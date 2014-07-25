package scripts.MinerPlusPlus;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSObject;

import scripts.Miner;

public class GUI {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
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

	public JFrame frmMiner;
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
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	public void addOres(String s) {

		getList_Ores().add(s);

	}

	public JButton getBtnAddToSelected() {
		if (btnAddToSelected == null) {
			btnAddToSelected = new JButton(">>");
			btnAddToSelected.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					if (list_Ores.getSelectedItem().length() > 0) {
						list_selectedores.add(list_Ores.getSelectedItem());
						Set<Integer> IDs = new HashSet<>();

						for (String s : list_selectedores.getItems()) {

							IDs.add(Integer.valueOf(s));

						}

						Miner.selectedOres = Miner.miner.SetToArray(IDs);

					}

				}
			});
			btnAddToSelected.setFont(new Font("Tahoma", Font.PLAIN, 8));
			btnAddToSelected.setHorizontalAlignment(SwingConstants.LEFT);
			btnAddToSelected.setBounds(118, 355, 46, 23);
		}
		return btnAddToSelected;
	}

	public JButton getBtnRefresh() {
		if (btnRefresh == null) {
			btnRefresh = new JButton("Refresh");
			btnRefresh.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					System.out.println("Refreshing");
					System.out.println("updating ores");

					System.out.println("passed check");

					RSObject[] Objs = Objects.find(50, "Rocks");
					System.out.println("passed objs");
					Set<Integer> IDs = new HashSet<>();
					System.out.println("passed hash");

					for (RSObject obj : Objs) {

						System.out.println("passed objs");
						IDs.add(obj.getDefinition().getID());
						System.out.println("passed adding id");
					}

					for (Integer inte : IDs) {

						getList_Ores().add("" + inte);
						System.out.println("passed adding to list");
					}

				}
			});
			btnRefresh.setBounds(20, 472, 89, 23);
		}
		return btnRefresh;
	}

	public JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton("Remove");
			btnRemove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					list_selectedores.remove(list_selectedores
							.getSelectedIndex());

					Set<Integer> IDs = new HashSet<>();

					for (String s : list_selectedores.getItems()) {

						IDs.add(Integer.valueOf(s));

					}

					Miner.selectedOres = Miner.miner.SetToArray(IDs);

				}
			});
			btnRemove.setBounds(172, 472, 89, 23);
		}
		return btnRemove;
	}

	public JLabel getLblInfo() {
		if (lblInfo == null) {
			lblInfo = new JLabel("Info");
			lblInfo.setBounds(52, 11, 46, 14);
		}
		return lblInfo;
	}

	public JLabel getLblOres() {
		if (lblOres == null) {
			lblOres = new JLabel("Available Ores");
			lblOres.setBounds(10, 257, 110, 14);
		}
		return lblOres;
	}

	public JLabel getLblSelectedOres() {
		if (lblSelectedOres == null) {
			lblSelectedOres = new JLabel("Selected Ores");
			lblSelectedOres.setBounds(162, 257, 90, 14);
		}
		return lblSelectedOres;
	}

	@SuppressWarnings("deprecation")
	public List getList_Ores() {
		if (list_Ores == null) {
			list_Ores = new List();
			list_Ores.setMultipleSelections(false);
			list_Ores.setBounds(10, 275, 110, 196);
		}
		return list_Ores;
	}

	@SuppressWarnings("deprecation")
	public List getList_selectedores() {
		if (list_selectedores == null) {
			list_selectedores = new List();
			list_selectedores.setMultipleSelections(false);
			list_selectedores.setBounds(162, 275, 110, 196);
		}
		return list_selectedores;
	}

	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getTxtrSfsdf());
			scrollPane.setBounds(49, 27, 209, 58);
		}
		return scrollPane;
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

	/**
	 */
	private void initialize() {
		frmMiner = new JFrame();
		frmMiner.setTitle("Miner++");
		frmMiner.setResizable(false);
		frmMiner.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmMiner.setBounds(100, 100, 288, 603);
		frmMiner.getContentPane().setLayout(null);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (chckbxHighlightModels.isSelected()) {

					Miner.highlightModel = true;

				}

				if (chckbxHighlightWalkPath.isSelected()) {

					Miner.highlightPath = true;

				}

				if (chckbxDrawHud.isSelected()) {

					Miner.drawHUD = true;

				}

				if (chckbxAdvancedHud.isSelected()) {

					Miner.advancedHUD = true;

				}

				if (comboBox.getSelectedItem().equals("Bank")) {

					Miner.method = MiningMethod.BANKING;

				}

				if (comboBox.getSelectedItem().equals("Powermine")) {

					Miner.method = MiningMethod.POWERMINE;

				}

				if (comboBox.getSelectedItem().equals("M1D1")) {

					Miner.method = MiningMethod.M1D1;

				}

				Miner.guiIsComplete = true;
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
		chckbxAntiban.setSelected(true);
		chckbxAntiban.setBounds(52, 123, 97, 23);
		frmMiner.getContentPane().add(chckbxAntiban);

		chckbxHighlightModels = new JCheckBox("Highlight Models");
		chckbxHighlightModels.setSelected(true);
		chckbxHighlightModels.setBounds(49, 149, 150, 23);
		frmMiner.getContentPane().add(chckbxHighlightModels);

		chckbxDrawHud = new JCheckBox("Draw HUD");
		chckbxDrawHud.setSelected(true);
		chckbxDrawHud.setBounds(52, 175, 112, 23);
		frmMiner.getContentPane().add(chckbxDrawHud);

		chckbxHighlightWalkPath = new JCheckBox("Highlight Walk Path");
		chckbxHighlightWalkPath.setSelected(true);
		chckbxHighlightWalkPath.setBounds(49, 201, 150, 23);
		frmMiner.getContentPane().add(chckbxHighlightWalkPath);

		chckbxAdvancedHud = new JCheckBox("Advanced HUD");
		chckbxAdvancedHud.setSelected(true);
		chckbxAdvancedHud.setBounds(52, 227, 125, 23);
		frmMiner.getContentPane().add(chckbxAdvancedHud);
		frmMiner.getContentPane().add(getLblOres());
		frmMiner.getContentPane().add(getList_Ores());
		frmMiner.getContentPane().add(getList_selectedores());
		frmMiner.getContentPane().add(getLblSelectedOres());
		frmMiner.getContentPane().add(getLblInfo());
		frmMiner.getContentPane().add(getTxtrSfsdf());
		frmMiner.getContentPane().add(getBtnAddToSelected());
		frmMiner.getContentPane().add(getBtnRemove());
		frmMiner.getContentPane().add(getBtnRefresh());
		frmMiner.getContentPane().add(getScrollPane());
	}
}
