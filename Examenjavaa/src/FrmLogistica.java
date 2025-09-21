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

// Asegúrate de que los imports de tus clases del modelo sean correctos
import modelos.Envio;
import modelos.EnvioAereo;
import modelos.EnvioMaritimo;
import modelos.EnvioTerrestre;

public class FrmLogistica extends JFrame {

    public String[] encabezados = new String[]{"Tipo", "Código", "Cliente", "Peso", "Distancia", "Costo"};

    private JTable tblEnvios;
    private JPanel pnlEditarEnvio;
    private JTextField txtNumero, txtCliente, txtPeso, txtDistancia;
    private JComboBox<String> cmbTipoEnvio;
    private ArrayList<Envio> listaEnvios;

    public FrmLogistica() {
        setSize(700, 500);
        setTitle("Operador Logístico");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        listaEnvios = new ArrayList<>();

        JToolBar tbLogistica = new JToolBar();

        // ===== TU CÓDIGO DE BOTONES CON ICONOS =====
        JButton btnAgregarEnvio = new JButton();
        btnAgregarEnvio.setIcon(new ImageIcon(getClass().getResource("/iconos/AgregarEnvio.png")));
        btnAgregarEnvio.setToolTipText("Agregar Envío");
        btnAgregarEnvio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                agregarEnvio();
            }
        });

        JButton btnQuitarEnvio = new JButton();
        btnQuitarEnvio.setIcon(new ImageIcon(getClass().getResource("/iconos/QuitarEnvio.png")));
        btnQuitarEnvio.setToolTipText("Quitar Envío");
        btnQuitarEnvio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                quitarEnvio();
            }
        });
        // ==========================================

        tbLogistica.add(btnAgregarEnvio);
        tbLogistica.add(btnQuitarEnvio);

        JPanel pnlEnvios = new JPanel(new BorderLayout());

        pnlEditarEnvio = new JPanel();
        pnlEditarEnvio.setPreferredSize(new Dimension(0, 120));
        pnlEditarEnvio.setLayout(null);

        pnlEditarEnvio.add(new JLabel("Número")).setBounds(10, 10, 100, 25);
        txtNumero = new JTextField();
        txtNumero.setBounds(120, 10, 150, 25);
        pnlEditarEnvio.add(txtNumero);

        pnlEditarEnvio.add(new JLabel("Tipo")).setBounds(300, 10, 120, 25);
        cmbTipoEnvio = new JComboBox<>();
        cmbTipoEnvio.setModel(new DefaultComboBoxModel<>(new String[]{"Terrestre", "Aereo", "Maritimo"}));
        cmbTipoEnvio.setBounds(430, 10, 150, 25);
        pnlEditarEnvio.add(cmbTipoEnvio);

        pnlEditarEnvio.add(new JLabel("Cliente")).setBounds(10, 40, 100, 25);
        txtCliente = new JTextField();
        txtCliente.setBounds(120, 40, 150, 25);
        pnlEditarEnvio.add(txtCliente);

        pnlEditarEnvio.add(new JLabel("Distancia en Km")).setBounds(300, 40, 120, 25);
        txtDistancia = new JTextField();
        txtDistancia.setBounds(430, 40, 150, 25);
        pnlEditarEnvio.add(txtDistancia);

        pnlEditarEnvio.add(new JLabel("Peso en Kg")).setBounds(10, 70, 100, 25);
        txtPeso = new JTextField();
        txtPeso.setBounds(120, 70, 150, 25);
        pnlEditarEnvio.add(txtPeso);

        JButton btnGuardarEnvio = new JButton("Guardar");
        btnGuardarEnvio.setBounds(300, 70, 120, 25);
        btnGuardarEnvio.addActionListener(e -> guardarEnvio());
        pnlEditarEnvio.add(btnGuardarEnvio);

        JButton btnCancelarEnvio = new JButton("Cancelar");
        btnCancelarEnvio.setBounds(430, 70, 150, 25);
        btnCancelarEnvio.addActionListener(e -> cancelarEnvio());
        pnlEditarEnvio.add(btnCancelarEnvio);

        pnlEditarEnvio.setVisible(false);

        tblEnvios = new JTable();
        JScrollPane spListaEnvios = new JScrollPane(tblEnvios);

        pnlEnvios.add(pnlEditarEnvio, BorderLayout.NORTH);
        pnlEnvios.add(spListaEnvios, BorderLayout.CENTER);

        getContentPane().add(tbLogistica, BorderLayout.NORTH);
        getContentPane().add(pnlEnvios, BorderLayout.CENTER);

        actualizarTabla();
    }

    public void agregarEnvio() {
        limpiarCampos();
        pnlEditarEnvio.setVisible(true);
    }

    public void quitarEnvio() {
        int filaSeleccionada = tblEnvios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            listaEnvios.remove(filaSeleccionada);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un envío para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void guardarEnvio() {
        try {
            String codigo = txtNumero.getText();
            String cliente = txtCliente.getText();
            double peso = Double.parseDouble(txtPeso.getText());
            double distancia = Double.parseDouble(txtDistancia.getText());
            String tipo = (String) cmbTipoEnvio.getSelectedItem();

            Envio nuevoEnvio = null;
            
            // Usando los nombres de tus clases: EnvioTerrestre, EnvioAereo, EnvioMaritimo
            switch (tipo) {
                case "Terrestre":
                    nuevoEnvio = new EnvioTerrestre(codigo, cliente, peso, distancia);
                    break;
                case "Aereo":
                    nuevoEnvio = new EnvioAereo(codigo, cliente, peso, distancia);
                    break;
                case "Maritimo":
                    nuevoEnvio = new EnvioMaritimo(codigo, cliente, peso, distancia);
                    break;
            }

            if (nuevoEnvio != null) {
                listaEnvios.add(nuevoEnvio);
                actualizarTabla();
                pnlEditarEnvio.setVisible(false);
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos para Peso y Distancia.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cancelarEnvio() {
        pnlEditarEnvio.setVisible(false);
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtNumero.setText("");
        txtCliente.setText("");
        txtPeso.setText("");
        txtDistancia.setText("");
        cmbTipoEnvio.setSelectedIndex(0);
    }

    private void actualizarTabla() {
        DefaultTableModel dtm = new DefaultTableModel(null, encabezados);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (Envio envio : listaEnvios) {
            String tipo = "";
            if (envio instanceof EnvioTerrestre) tipo = "Terrestre";
            else if (envio instanceof EnvioAereo) tipo = "Aereo";
            else if (envio instanceof EnvioMaritimo) tipo = "Maritimo";

            Object[] fila = new Object[]{
                tipo,
                envio.getCodigo(),
                envio.getCliente(),
                envio.getPeso(),
                envio.getDistancia(),
                df.format(envio.calcularTarifa())
            };
            dtm.addRow(fila);
        }
        tblEnvios.setModel(dtm);
    }
}
