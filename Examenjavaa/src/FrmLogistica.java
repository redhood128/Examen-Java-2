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

public class FrmLogistica extends JFrame {

    public String[] encabezados = new String[] { "Código", "Cliente", "Peso", "Distancia", "Tipo", "Costo" };

    private JTable tblEnvios;
    private JPanel pnlEditarEnvio;

    private JTextField txtNumero, txtCliente, txtPeso, txtDistancia;
    private JComboBox<String> cmbTipoEnvio;

    //almacenar los envíos en memoria
    private ArrayList<Envio> listaEnvios;

    public FrmLogistica() {
        setSize(600, 400);
        setTitle("Operador Logísitico");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // lista de envíos
        listaEnvios = new ArrayList<>();

        JToolBar tbLogistica = new JToolBar();

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

        tbLogistica.add(btnAgregarEnvio);
        tbLogistica.add(btnQuitarEnvio);

        JPanel pnlEnvios = new JPanel();
        pnlEnvios.setLayout(new BoxLayout(pnlEnvios, BoxLayout.Y_AXIS));

        pnlEditarEnvio = new JPanel();
        pnlEditarEnvio.setPreferredSize(new Dimension(420, 300));
        pnlEditarEnvio.setLayout(null);

        JLabel lblNumero = new JLabel("Número");
        lblNumero.setBounds(10, 10, 100, 25);
        pnlEditarEnvio.add(lblNumero);

        txtNumero = new JTextField();
        txtNumero.setBounds(120, 10, 150, 25);
        pnlEditarEnvio.add(txtNumero);

        JLabel lblTipoEnvio = new JLabel("Tipo");
        lblTipoEnvio.setBounds(300, 10, 120, 25);
        pnlEditarEnvio.add(lblTipoEnvio);

        cmbTipoEnvio = new JComboBox<>();
        cmbTipoEnvio.setModel(new DefaultComboBoxModel<>(new String[] { "Terrestre", "Aéreo", "Marítimo" }));
        cmbTipoEnvio.setBounds(430, 10, 150, 25);
        pnlEditarEnvio.add(cmbTipoEnvio);

        JLabel lblCliente = new JLabel("Cliente");
        lblCliente.setBounds(10, 40, 100, 25);
        pnlEditarEnvio.add(lblCliente);

        txtCliente = new JTextField();
        txtCliente.setBounds(120, 40, 150, 25);
        pnlEditarEnvio.add(txtCliente);

        JLabel lblDistancia = new JLabel("Distancia en Km");
        lblDistancia.setBounds(300, 40, 120, 25);
        pnlEditarEnvio.add(lblDistancia);

        txtDistancia = new JTextField();
        txtDistancia.setBounds(430, 40, 150, 25);
        pnlEditarEnvio.add(txtDistancia);

        JLabel lblPeso = new JLabel("Peso en Kg");
        lblPeso.setBounds(10, 70, 100, 25);
        pnlEditarEnvio.add(lblPeso);

        txtPeso = new JTextField();
        txtPeso.setBounds(120, 70, 150, 25);
        pnlEditarEnvio.add(txtPeso);

        JButton btnGuardarEnvio = new JButton("Guardar");
        btnGuardarEnvio.setBounds(300, 70, 120, 25);
        btnGuardarEnvio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guardarEnvio();
            }
        });
        pnlEditarEnvio.add(btnGuardarEnvio);

        JButton btnCancelarEnvio = new JButton("Cancelar");
        btnCancelarEnvio.setBounds(430, 70, 150, 25);
        btnCancelarEnvio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelarEnvio();
            }
        });
        pnlEditarEnvio.add(btnCancelarEnvio);

        pnlEditarEnvio.setVisible(false); 

        tblEnvios = new JTable();
        JScrollPane spListaEnvios = new JScrollPane(tblEnvios);

        DefaultTableModel dtm = new DefaultTableModel(null, encabezados);
        tblEnvios.setModel(dtm);

        pnlEnvios.add(pnlEditarEnvio);
        pnlEnvios.add(spListaEnvios);

        JScrollPane spEnvios = new JScrollPane(pnlEnvios);
        spEnvios.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        getContentPane().add(tbLogistica, BorderLayout.NORTH);
        getContentPane().add(pnlEnvios, BorderLayout.CENTER);
    }

    public void agregarEnvio() {
        limpiarCampos();
        pnlEditarEnvio.setVisible(true);
    }

    public void quitarEnvio() {
        int filaSeleccionada = tblEnvios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Eliminar de la lista en memoria
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

            switch (tipo) {
                case "Terrestre":
                    nuevoEnvio = new EnvioTerrestre(codigo, cliente, peso, distancia);
                    break;
                case "Aéreo":
                    nuevoEnvio = new EnvioAereo(codigo, cliente, peso, distancia);
                    break;
                case "Marítimo":
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        DefaultTableModel dtm = (DefaultTableModel) tblEnvios.getModel();
        dtm.setRowCount(0); // Limpia la tabla antes de llenarla

        DecimalFormat df = new DecimalFormat("#.0");

        for (Envio envio : listaEnvios) {
            Object[] fila = new Object[] {
                envio.getCodigo(),
                envio.getCliente(),
                envio.getPeso(),
                envio.getDistancia(),
                envio.getTipo(),
                df.format(envio.calcularTarifa()) // Valor formateado
            };
            dtm.addRow(fila);
        }
    }

}

