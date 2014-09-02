package io.github.avatarhurden.tribalwarsengine.ferramentas.distância;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;
import io.github.avatarhurden.tribalwarsengine.panels.Ferramenta;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import config.Lang;
import database.BigOperation;
import database.Cores;

@SuppressWarnings("serial")
public class DistânciaPanel extends Ferramenta {
	
	private Army army;
	private ArmyEditPanel armyEdit;
	
	private OnChange onChange;
	
	private CoordenadaPanel aldeiaOrigem, aldeiaDestino;
	
	private PlanejadorHorárioPanel planejadorHorárioPanel;
	
	private long time;
	private TimeFormattedJLabel timeLabel;

	/**
	 * Calcula o tempo de deslocamento entre duas aldeias. Mostra o tempo de
	 * cada unidade.
	 */
	public DistânciaPanel() {
		super(Lang.FerramentaDistancia.toString());
		
		onChange = new OnChange() {	
			public void run() {
				armyEdit.saveValues();
				calculateDistanceAndTimes();
				planejadorHorárioPanel.changeDate(time);
			}
		};
		
		army = new Army(Army.getAttackingUnits());
		armyEdit = army.getEditPanelSelection(onChange);
		
		aldeiaDestino = new CoordenadaPanel(Lang.AldeiaDestino.toString()) {
			public void go() {
				calculateDistanceAndTimes();
			}
		};
		
		aldeiaOrigem = new CoordenadaPanel(Lang.AldeiaOrigem.toString()) {
			public void go() {
				calculateDistanceAndTimes();
			}
		};
		
		makeGUI();
	}
	
	private void makeGUI() {
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0, 0 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		c.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), c);
		
		c.gridy++;
		c.gridx = 0;
		c.gridheight = 3;
		c.insets = new Insets(5, 5, 5, 25);
		add(armyEdit, c);
		
		// Adiciona aldeia de origem
		c.gridheight = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx++;
		c.insets = new Insets(5, 5, 5, 5);
		add(aldeiaOrigem, c);
		
		// Adiciona aldeia de destino
		c.gridx++;
		add(aldeiaDestino, c);
		
		c.gridx--;
		c.gridy++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		//Foi a única maneira que achei pra fazer funcionar
		c.insets = new Insets(40, 5, 5, 5);
		add(createTimePanel(), c);
		
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.SOUTH;
		c.gridwidth = 2;
		c.insets = new Insets(5, 5, 5, 5);
		add(planejadorHorárioPanel = new PlanejadorHorárioPanel(onChange), c);
		
		c.gridx += 2;
		//add(tools.addAlertCreatorPanel(planejadorHorárioPanel.getDateLabel(), aldeiaOrigem, 
		//		aldeiaDestino, null), c);
	}

	protected void calculateDistanceAndTimes() {
		
			int diferençaX = aldeiaOrigem.getCoordenadaX()
					- aldeiaDestino.getCoordenadaX();
			int diferençaY = aldeiaOrigem.getCoordenadaY()
					- aldeiaDestino.getCoordenadaY();

			BigDecimal xSquared = new BigDecimal(diferençaX).pow(2);
			BigDecimal ySquared = new BigDecimal(diferençaY).pow(2);

			BigDecimal distância = BigOperation.sqrt(xSquared.add(ySquared), 30);
			
			time = distância.multiply(new BigDecimal(
					Math.round(army.getVelocidade()))).longValue();
			
			timeLabel.setTime(time);
			
			planejadorHorárioPanel.changeDate(time);
			
	}
	
	private JPanel createTimePanel() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.setOpaque(false);
		
		JPanel ataquePanel = new JPanel();
		ataquePanel.add(new JLabel("Tempo de Deslocamento"));
			
		ataquePanel.setBackground(Cores.FUNDO_ESCURO);
		ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		panel.add(ataquePanel);
				
		timeLabel = new TimeFormattedJLabel(true);
		
		JPanel horaPanel = new JPanel();
		horaPanel.add(timeLabel);
						
		horaPanel.setBackground(Cores.ALTERNAR_ESCURO);
		horaPanel.setBorder(new MatteBorder(0, 1, 1, 1,Cores.SEPARAR_ESCURO));
				
		panel.add(horaPanel);
		
		return panel;
	}
	
	private ActionListener getResetButtonAction() {	
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aldeiaOrigem.reset();
				aldeiaDestino.reset();
				armyEdit.resetComponents();
			}
		};
		
		return action;
	}
}
