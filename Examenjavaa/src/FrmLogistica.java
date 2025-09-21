import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import modelos.Envio;
import modelos.EnvioTerrestre;
import modelos.EnvioAereo;
import modelos.EnvioMaritimo;

public class FrmLogistica extends JFrame {

    public String[] columnasTabla = new String[]{"Tipo", "Código", "Cliente", "Peso", "Distancia", "Costo"};

    private JTable tablaEnvios;
    private JPanel panelFormulario;

    private JTextField campoCodigo, campoCliente, campoPeso, campoDistancia;
    private JComboBox<String> selectorTipo;

    private ArrayList<Envio> almacenDeEnvios;

    public FrmLogistica() {
        setSize(700, 500);
        setTitle("Operador Logístico");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        almacenDeEnvios = new ArrayList<>();

        JToolBar barraDeAcciones = new JToolBar();
        JButton botonAgregar = new JButton("Agregar Envío");
        JButton botonQuitar = new JButton("Quitar Envío");
        barraDeAcciones.add(botonAgregar);
        barraDeAcciones.add(botonQuitar);

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        panelFormulario.setPreferredSize(new Dimension(0, 120));

        panelFormulario.add(new JLabel("Número:")).setBounds(15, 20, 100, 25);
        campoCodigo = new JTextField();
        campoCodigo.setBounds(80, 20, 120, 25);
        panelFormulario.add(campoCodigo);

        panelFormulario.add(new JLabel("Cliente:")).setBounds(15, 55, 100, 25);
        campoCliente = new JTextField();
        campoCliente.setBounds(80, 55, 120, 25);
        panelFormulario.add(campoCliente);

        panelFormulario.add(new JLabel("Tipo:")).setBounds(230, 20, 100, 25);
        selectorTipo = new JComboBox<>(new String[]{"Terrestre", "Aereo", "Maritimo"});
        selectorTipo.setBounds(340, 20, 120, 25);
        panelFormulario.add(selectorTipo);

        panelFormulario.add(new JLabel("Distancia (Km):")).setBounds(230, 55, 100, 25);
        campoDistancia = new JTextField();
        campoDistancia.setBounds(340, 55, 120, 25);
        panelFormulario.add(campoDistancia);

        panelFormulario.add(new JLabel("Peso (Kg):")).setBounds(490, 20, 100, 25);
        campoPeso = new JTextField();
        campoPeso.setBounds(570, 20, 100, 25);
        panelFormulario.add(campoPeso);

        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(490, 55, 90, 25);
        panelFormulario.add(botonGuardar);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBounds(590, 55, 90, 25);
        panelFormulario.add(botonCancelar);

        panelFormulario.setVisible(false);

        tablaEnvios = new JTable();
        JScrollPane panelConScroll = new JScrollPane(tablaEnvios);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(panelConScroll, BorderLayout.CENTER);

        getContentPane().add(barraDeAcciones, BorderLayout.NORTH);
        getContentPane().add(panelPrincipal, BorderLayout.CENTER);

        botonAgregar.addActionListener(e -> activarFormulario());
        botonQuitar.addActionListener(e -> removerEnvio());
        botonGuardar.addActionListener(e -> registrarEnvio());
        botonCancelar.addActionListener(e -> desactivarFormulario());

        pintarTabla();
    }

    public void activarFormulario() {
        vaciarCampos();
        panelFormulario.setVisible(true);
    }

    public void desactivarFormulario() {
        panelFormulario.setVisible(false);
    }

    public void removerEnvio() {
        int filaSeleccionada = tablaEnvios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            almacenDeEnvios.remove(filaSeleccionada);
            pintarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un envío de la lista.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void registrarEnvio() {
        try {
            double peso = Double.parseDouble(campoPeso.getText());
            double distancia = Double.parseDouble(campoDistancia.getText());
            String codigo = campoCodigo.getText();
            String cliente = campoCliente.getText();
            String tipo = (String) selectorTipo.getSelectedItem();

            Envio nuevoIngreso = null;
            if (tipo.equals("Terrestre")) {
                nuevoIngreso = new EnvioTerrestre(codigo, cliente, peso, distancia);
            } else if (tipo.equals("Aereo")) {
                nuevoIngreso = new EnvioAereo(codigo, cliente, peso, distancia);
            } else if (tipo.equals("Maritimo")) {
                nuevoIngreso = new EnvioMaritimo(codigo, cliente, peso, distancia);
            }

            if (nuevoIngreso != null) {
                almacenDeEnvios.add(nuevoIngreso);
                pintarTabla();
                desactivarFormulario();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El peso y la distancia deben ser números.", "Error de Entrada", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void vaciarCampos() {
        campoCodigo.setText("");
        campoCliente.setText("");
        campoPeso.setText("");
        campoDistancia.setText("");
        selectorTipo.setSelectedIndex(0);
    }

    private void pintarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(null, columnasTabla);
        DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

        for (Envio envioActual : almacenDeEnvios) {
            String tipoEnvio = "";
            if (envioActual instanceof EnvioTerrestre) tipoEnvio = "Terrestre";
            else if (envioActual instanceof EnvioAereo) tipoEnvio = "Aereo";
            else if (envioActual instanceof EnvioMaritimo) tipoEnvio = "Maritimo";
            
            Object[] fila = new Object[]{
                tipoEnvio,
                envioActual.getCodigo(),
                envioActual.getCliente(),
                envioActual.getPeso(),
                envioActual.getDistancia(),
                formatoMoneda.format(envioActual.calcularTarifa())
            };
            modelo.addRow(fila);
        }
        tablaEnvios.setModel(modelo);
    }
}
