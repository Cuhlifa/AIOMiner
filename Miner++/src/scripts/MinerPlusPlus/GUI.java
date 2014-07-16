package scripts.MinerPlusPlus;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JCheckBox;

import javax.swing.JLabel;

import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import scripts.Miner;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import java.awt.List;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

public class GUI {

	public JFrame frmMiner;
	public Miner miner;
	public JButton btnStart;
	public JComboBox comboBox;
	public JCheckBox chckbxAntiban;
	public JCheckBox chckbxHighlightModels;
	public JCheckBox chckbxDrawHud;
	public JCheckBox chckbxHighlightWalkPath;
	public JCheckBox chckbxAdvancedHud;
	private JLabel lblOres;
	private List list_Ores;
	private List list_selectedores;
	private JLabel lblSelectedOres;
	private JLabel lblInfo;
	private JTextArea txtrSfsdf;
	private JButton btnAddToSelected;
	private JButton btnRemove;
	private JButton btnRefresh;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmMiner.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	public GUI(Miner main) {
		miner = main;
		initialize();
	}

	/**
	 */
	private void initialize() {
		frmMiner = new JFrame();
		frmMiner.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("FavIcon.png")));
		frmMiner.setTitle("Miner++");
		frmMiner.setResizable(false);
		frmMiner.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmMiner.setBounds(100, 100, 288, 603);
		frmMiner.getContentPane().setLayout(null);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (chckbxHighlightModels.isSelected()) {

					miner.HighlightModel = true;

				}

				if (chckbxHighlightWalkPath.isSelected()) {

					miner.HighlightPath = true;

				}

				if (chckbxDrawHud.isSelected()) {

					miner.DrawHUD = true;

				}

				if (chckbxAdvancedHud.isSelected()) {

					miner.AdvancedHUD = true;

				}

				if (comboBox.getSelectedItem().equals("Bank")) {

					miner.method = MiningMethod.BANKING;

				}

				if (comboBox.getSelectedItem().equals("Powermine")) {

					miner.method = MiningMethod.POWERMINE;

				}

				if (comboBox.getSelectedItem().equals("M1D1")) {

					miner.method = MiningMethod.M1D1;

				}

				miner.GUI_ISCOMPLETE = true;
				frmMiner.dispose();

			}
		});

		btnStart.setBounds(40, 506, 200, 50);
		frmMiner.getContentPane().add(btnStart);

		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Bank",
				"M1D1", "Powermine" }));
		comboBox.setBounds(56, 96, 173, 20);
		frmMiner.getContentPane().add(comboBox);

		chckbxAntiban = new JCheckBox("Anti-Ban");
		this.chckbxAntiban.setSelected(true);
		chckbxAntiban.setBounds(52, 123, 97, 23);
		this.frmMiner.getContentPane().add(chckbxAntiban);

		chckbxHighlightModels = new JCheckBox("Highlight Models");
		this.chckbxHighlightModels.setSelected(true);
		chckbxHighlightModels.setBounds(49, 149, 150, 23);
		this.frmMiner.getContentPane().add(chckbxHighlightModels);

		chckbxDrawHud = new JCheckBox("Draw HUD");
		this.chckbxDrawHud.setSelected(true);
		chckbxDrawHud.setBounds(52, 175, 112, 23);
		this.frmMiner.getContentPane().add(chckbxDrawHud);

		chckbxHighlightWalkPath = new JCheckBox("Highlight Walk Path");
		this.chckbxHighlightWalkPath.setSelected(true);
		chckbxHighlightWalkPath.setBounds(49, 201, 150, 23);
		this.frmMiner.getContentPane().add(chckbxHighlightWalkPath);

		this.chckbxAdvancedHud = new JCheckBox("Advanced HUD");
		this.chckbxAdvancedHud.setSelected(true);
		this.chckbxAdvancedHud.setBounds(52, 227, 125, 23);
		this.frmMiner.getContentPane().add(this.chckbxAdvancedHud);
		this.frmMiner.getContentPane().add(getLblOres());
		this.frmMiner.getContentPane().add(getList_Ores());
		this.frmMiner.getContentPane().add(getList_selectedores());
		this.frmMiner.getContentPane().add(getLblSelectedOres());
		this.frmMiner.getContentPane().add(getLblInfo());
		this.frmMiner.getContentPane().add(getTxtrSfsdf());
		this.frmMiner.getContentPane().add(getBtnAddToSelected());
		this.frmMiner.getContentPane().add(getBtnRemove());
		this.frmMiner.getContentPane().add(getBtnRefresh());
		this.frmMiner.getContentPane().add(getScrollPane());
	}

	public JLabel getLblOres() {
		if (lblOres == null) {
			lblOres = new JLabel("Available Ores");
			lblOres.setBounds(10, 257, 110, 14);
		}
		return lblOres;
	}

	public List getList_Ores() {
		if (list_Ores == null) {
			list_Ores = new List();
			list_Ores.setMultipleSelections(false);
			list_Ores.setBounds(10, 275, 110, 196);
		}
		return list_Ores;
	}

	public List getList_selectedores() {
		if (list_selectedores == null) {
			list_selectedores = new List();
			list_selectedores.setMultipleSelections(false);
			list_selectedores.setBounds(162, 275, 110, 196);
		}
		return list_selectedores;
	}

	public JLabel getLblSelectedOres() {
		if (lblSelectedOres == null) {
			lblSelectedOres = new JLabel("Selected Ores");
			lblSelectedOres.setBounds(162, 257, 90, 14);
		}
		return lblSelectedOres;
	}

	public JLabel getLblInfo() {
		if (lblInfo == null) {
			lblInfo = new JLabel("Info");
			lblInfo.setBounds(52, 11, 46, 14);
		}
		return lblInfo;
	}

	public JTextArea getTxtrSfsdf() {
		if (txtrSfsdf == null) {
			txtrSfsdf = new JTextArea();
			txtrSfsdf.setFont(new Font("Monospaced", Font.PLAIN, 10));
			txtrSfsdf.setEditable(false);
			txtrSfsdf
					.setText("Red = Available\r\nYellow = Added to ore list\r\nGreen = Mining\r\nBlue = Next to mine\r\nPink = Selected in ore list");
			txtrSfsdf.setBounds(52, 27, 177, 58);
		}
		return txtrSfsdf;
	}

	public JButton getBtnAddToSelected() {
		if (btnAddToSelected == null) {
			btnAddToSelected = new JButton(">>");
			btnAddToSelected.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					if (list_Ores.getSelectedItem().length() > 0) {
						list_selectedores.add(list_Ores.getSelectedItem());
						Set<Integer> IDs = new HashSet<>();

						for (String s : list_selectedores.getItems()) {

							IDs.add(Integer.valueOf(s));

						}

						miner.SelectedOres = miner.SetToArray(IDs);

					}

				}
			});
			btnAddToSelected.setFont(new Font("Tahoma", Font.PLAIN, 8));
			btnAddToSelected.setHorizontalAlignment(SwingConstants.LEFT);
			btnAddToSelected.setBounds(118, 355, 46, 23);
		}
		return btnAddToSelected;
	}

	public JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton("Remove");
			btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					list_selectedores.remove(list_selectedores
							.getSelectedIndex());

					Set<Integer> IDs = new HashSet<>();

					for (String s : list_selectedores.getItems()) {

						IDs.add(Integer.valueOf(s));

					}

					miner.SelectedOres = miner.SetToArray(IDs);

				}
			});
			btnRemove.setBounds(172, 472, 89, 23);
		}
		return btnRemove;
	}

	public JButton getBtnRefresh() {
		if (btnRefresh == null) {
			btnRefresh = new JButton("Refresh");
			btnRefresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					miner.updateOres();

				}
			});
			btnRefresh.setBounds(20, 472, 89, 23);
		}
		return btnRefresh;
	}

	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getTxtrSfsdf());
			scrollPane.setBounds(49, 27, 209, 58);
		}
		return scrollPane;
	}
}
