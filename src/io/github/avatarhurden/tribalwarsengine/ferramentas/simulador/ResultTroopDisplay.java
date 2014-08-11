package io.github.avatarhurden.tribalwarsengine.ferramentas.simulador;

import io.github.avatarhurden.tribalwarsengine.ferramentas.simulador.SimuladorPanel.OutputInfo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import config.Lang;
import config.Mundo_Reader;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class ResultTroopDisplay extends JPanel {

	private OutputInfo output;

	private Map<Unidade, JLabel> tropasAtacantesPerdidas = new HashMap<Unidade, JLabel>();

	private Map<Unidade, JLabel> tropasDefensorasPerdidas = new HashMap<Unidade, JLabel>();

	private JLabel muralha, edif�cio;

	public ResultTroopDisplay(OutputInfo output) {

		this.output = output;

		setOpaque(false);

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {};
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		// Panel that says that the shown units are lost units
		JPanel identificationPanel = new JPanel();
		identificationPanel.setBackground(Cores.FUNDO_ESCURO);
		identificationPanel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		identificationPanel.add(new JLabel(Lang.UnidadesPerdidas.toString()));
		
		identificationPanel.setPreferredSize(new Dimension(
				identificationPanel.getPreferredSize().width-2, identificationPanel.getPreferredSize().height-3));
		
		c.insets = new Insets(0, 5, 1, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		add(identificationPanel, c);
		
		c.gridwidth = 1;
		c.gridy++;
		c.insets = new Insets(0, 5, 5, 6);
		add(addUnitNames(), c);
		
		// Panel to store both attack and defense unit panels
		JPanel unitPanels = new JPanel(new GridBagLayout());
		// unitPanels.setBorder(new MatteBorder(1,0,0,0,Cores.SEPARAR_ESCURO));
		unitPanels.setOpaque(false);

		GridBagConstraints unitC = new GridBagConstraints();
		unitC.insets = new Insets(0, 0, 0, 0);
		unitC.anchor = GridBagConstraints.NORTH;

		unitPanels.add(addAttackUnitPanel(), unitC);
		unitC.gridy++;
		unitPanels.add(addDefenseUnitPanel(), unitC);

		c.gridx++;
		c.insets = new Insets(0, 0, 5, 0);
		c.gridwidth = 2;
		add(unitPanels, c);

		// c.gridx++;
		// add(addDefenseUnitPanel(), c);

		// Panel to store the wall and building panels, so they center nicely
		JPanel bottomPanels = new JPanel();
		bottomPanels.setOpaque(false);

		bottomPanels.add(addWall());
		bottomPanels.add(addBuilding());

		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 3;
		add(bottomPanels, c);

	}

	public JPanel addUnitNames() {

		JPanel panel = new JPanel();
		// panel.setBackground(Cores.FUNDO_ESCURO);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;

		// Adding the headers

		JLabel lblNome = new JLabel(Lang.Unidade.toString());
		lblNome.setPreferredSize(new Dimension(
				lblNome.getPreferredSize().width + 12, 26));
		lblNome.setBackground(Cores.FUNDO_ESCURO);
		lblNome.setOpaque(true);
		lblNome.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(lblNome, c);

		// Setting the constraints for the unit panels
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;

		int loop = 1;

		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {

			if (i != null) {

				JPanel tropaPanel = new JPanel();
				tropaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				tropaPanel.setBackground(Cores.getAlternar(loop));

				// Separa��o entre a parte de nomenclatura e as unidades
				if (i.equals(Unidade.LANCEIRO))
					tropaPanel.setBorder(new
							MatteBorder(1,0,0,0,Cores.SEPARAR_ESCURO));

				// Creating the TextField for the quantity of troops
				JLabel lbl = new JLabel(i.nome());

				tropaPanel.add(lbl);

				loop++;
				c.gridy++;
				panel.add(tropaPanel, c);

			}
		}

		return panel;

	}

	public JPanel addAttackUnitPanel() {

		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_ESCURO);
		panel.setBorder(new MatteBorder(1, 1, 1, 0, Cores.SEPARAR_ESCURO));
		// panel.setOpaque(false);

		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;

		// Adding the headers

		JLabel lblNome = new JLabel(Lang.Atacante.toString());
		lblNome.setPreferredSize(new Dimension(
				lblNome.getPreferredSize().width + 10, 26));
		lblNome.setBackground(Cores.FUNDO_ESCURO);
		lblNome.setOpaque(true);
		lblNome.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(lblNome, c);

		// Setting the constraints for the unit panels
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;

		int loop = 1;

		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {

			if (i != null && !i.equals(Unidade.MIL�CIA)) {

				JPanel tropaPanel = new JPanel();
				tropaPanel.setLayout(new GridBagLayout());
				tropaPanel.setBackground(Cores.getAlternar(loop));

				// Separa��o entre a parte de nomenclatura e as unidades
				if (i.equals(Unidade.LANCEIRO))
					tropaPanel.setBorder(new
							MatteBorder(1,0,0,0,Cores.SEPARAR_ESCURO));

				GridBagConstraints tropaC = new GridBagConstraints();
				tropaC.insets = new Insets(5, 5, 5, 5);
				tropaC.gridx = 0;
				tropaC.gridy = 0;

				// Creating the TextField for the quantity of troops
				JLabel lbl = new JLabel(" ");
				// Adding the text to a map with the units
				tropasAtacantesPerdidas.put(i, lbl);

				tropaPanel.add(lbl, tropaC);

				loop++;
				c.gridy++;
				panel.add(tropaPanel, c);

			}
		}

		return panel;

	}

	public JPanel addDefenseUnitPanel() {

		JPanel panel = new JPanel();
		panel.setBackground(Cores.FUNDO_ESCURO);
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;

		// Adding the headers

		JLabel lblNome = new JLabel(Lang.Defensor.toString());
		lblNome.setPreferredSize(new Dimension(
				lblNome.getPreferredSize().width + 10, 26));
		lblNome.setBackground(Cores.FUNDO_ESCURO);
		lblNome.setOpaque(true);
		lblNome.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(lblNome, c);

		// Setting the constraints for the unit panels
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;

		int loop = 1;

		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {

			if (i != null) {

				JPanel tropaPanel = new JPanel();
				tropaPanel.setLayout(new GridBagLayout());
				tropaPanel.setBackground(Cores.getAlternar(loop));

				// Separa��o entre a parte de nomenclatura e as unidades
				if (i.equals(Unidade.LANCEIRO))
					tropaPanel.setBorder(new
							MatteBorder(1,0,0,0,Cores.SEPARAR_ESCURO));

				GridBagConstraints tropaC = new GridBagConstraints();
				tropaC.insets = new Insets(5, 5, 5, 5);
				tropaC.gridx = 0;
				tropaC.gridy = 0;

				// Creating the TextField for the quantity of troops
				JLabel lbl = new JLabel(" ");
				// Adding the text to a map with the units
				tropasDefensorasPerdidas.put(i, lbl);

				tropaPanel.add(lbl, tropaC);

				loop++;
				c.gridy++;
				panel.add(tropaPanel, c);

			}
		}

		return panel;

	}

	public JPanel addWall() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		// Define a cor do panel com base no n�mero de tropas do mundo
		panel.setBackground(Cores.getAlternar(Mundo_Reader.MundoSelecionado
				.getN�meroDeTropas()+1));
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel(Lang.Muralha.toString()), c);

		muralha = new JLabel(" ");

		c.gridx++;
		panel.add(muralha, c);

		return panel;

	}

	public JPanel addBuilding() {

		// Setting the background and borders for the panel
		JPanel panel = new JPanel();
		// Define a cor do panel com base no n�mero de tropas do mundo
		panel.setBackground(Cores.getAlternar(Mundo_Reader.MundoSelecionado
				.getN�meroDeTropas()+1));
		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 80, 30 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel(Lang.Edificio.toString()), c);

		edif�cio = new JLabel(" ");

		c.gridx++;
		panel.add(edif�cio, c);

		return panel;

	}

	public void setValues() {

		NumberFormat numberFormat = NumberFormat
				.getNumberInstance(Locale.GERMANY);
		
		for (Entry<Unidade, JLabel> i : tropasAtacantesPerdidas.entrySet())
			i.getValue().setText(numberFormat.format(
					output.getTropasAtacantesPerdidas().get(i.getKey())));

		for (Entry<Unidade, JLabel> i : tropasDefensorasPerdidas.entrySet())
			i.getValue().setText(numberFormat.format(
					output.getTropasDefensorasPerdidas().get(i.getKey())));

		muralha.setText(String.valueOf(output.getMuralha()));

		edif�cio.setText(String.valueOf(output.getEdif�cio()));

	}
	
	public void resetValues() {
		
		for (Entry<Unidade, JLabel> i : tropasAtacantesPerdidas.entrySet())
			i.getValue().setText(" ");

		for (Entry<Unidade, JLabel> i : tropasDefensorasPerdidas.entrySet())
			i.getValue().setText(" ");

		muralha.setText(" ");

		edif�cio.setText(" ");
		
	}
	
}