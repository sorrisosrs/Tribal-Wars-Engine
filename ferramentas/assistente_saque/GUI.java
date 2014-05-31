package assistente_saque;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import assistente_saque.C�lculo.SameDateException;
import config.Mundo_Reader;
import custom_components.Ferramenta;
import custom_components.IntegerFormattedTextField;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta{

	// Objeto de c�lculo
	private C�lculo c�lculo;
	
	// Panels de inser��o de dados
	private PanelUnidade panelUnidades;
	private PanelIntervalo panelIntervalo;
	private PanelHor�rio panelHor�rio;
	
	// Panels para mostrar as respostas
	private JPanel panelRecomendado;
	private JPanel respostaHor�rio;
	
	private Map<Unidade, JLabel> mapRecomendado = new HashMap<Unidade, JLabel>();
	
	
	public GUI () {
		
		super("Assistente de Saque");
		
		// TODO make this default to ferramentas
		setBackground(Cores.FUNDO_CLARO);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		c.gridx = 0;
		
		c�lculo = new C�lculo();
		
		panelUnidades = new PanelUnidade();
		panelIntervalo = new PanelIntervalo();
		panelHor�rio = new PanelHor�rio();
		
		makePanelRecomendado();
		panelRecomendado.setVisible(false);
		
		// Add reset button
		c.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(getResetButtonAction()), c);
		
		// Add troop model button
		c.anchor = GridBagConstraints.CENTER;
		c.gridx++;
		add(tools.addModelosPanel(true, panelUnidades.getTextFields()));
		
		// Add units panel
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.NORTH;
		add(panelUnidades,c);
		
		// Add recomended units panel
		JPanel container = new JPanel(new GridLayout(1,0));
		container.setPreferredSize(panelRecomendado.getPreferredSize());
		container.setOpaque(false);
		container.add(panelRecomendado);
		
		c.gridx += 2;
		c.gridwidth = 1;
		add(container, c);
		
		// Where the "AldeiaModelos" panel will be added
		c.gridy = 0;
		c.gridx++;
		c.gridheight = 1;
		add(tools.addModelosPanel(true, null), c);
		
		// Add the panel that receives input for interval calculations 
		c.gridy++;
		add(panelIntervalo, c);
		
//		// Adds the panel that displays the interval in which to attack
//		c.gridy++;
//		add(respostaIntervalo, c);
		
		// Separar as duas fun��es
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setForeground(Cores.SEPARAR_ESCURO);
		
		c.gridx++;
		c.gridy = 1;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(0, 30, 0, 30);
		add(separator, c);
		
		// Where the "AldeiaModelos" panel will be added
		c.gridy = 0;
		c.gridx++;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5, 5, 5, 5);
		add(tools.addModelosPanel(true, null), c);
		
		// Add the panel that receives input for exact time calculations 
		c.gridy = 1;
		c.anchor = GridBagConstraints.NORTH;
		add(panelHor�rio, c);
		
		// Adds the panel that displays the time to send the attack 
		c.gridy++;
		
		JButton button = new JButton("Go");
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				c�lculo.setProdu��oEArmazenamento(panelIntervalo.getEdif�cios());
				c�lculo.setSaqueTotal(panelUnidades.getTextFields());
				c�lculo.setDist�ncia(panelHor�rio.getCoordenadaOrigem(), panelIntervalo.getCoordenadaDestino());
				c�lculo.setRestantes(panelHor�rio.getRecursosRestantes());
				c�lculo.setUltimoAtaque(panelHor�rio.getDataEnviada());
				
				c�lculo.setIntervalo();
				c�lculo.setEnviarAtaque();
				panelIntervalo.setDisplayIntervalo(c�lculo.getIntervalo());
				try {
					panelHor�rio.setDisplayHorario(c�lculo.getHorario());
				} catch (SameDateException exc) {
					panelHor�rio.setErrorMessage(exc.error);
				}
				setRecomendadoPanel(c�lculo.getUnidadesRecomendadas());
			}
		});
		
		add(button, c);
	}
	
	private ActionListener getResetButtonAction() {
		
		return new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				panelUnidades.resetTextFields();
				panelIntervalo.resetAll();
				panelHor�rio.resetAll();
				
			}
		};
		
	}
	
	private void makePanelRecomendado() {
		
		// TODO add a way for users to get help on what this is
		
		panelRecomendado = new JPanel(new GridBagLayout());
		panelRecomendado.setOpaque(false);
		panelRecomendado.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridy = 0;
		c.gridx = 0;
		
		// Adding "Recomendado" panel
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.add(new JLabel("Recomendado"));
		
		c.insets = new Insets(0, 0, 3, 0);
		panelRecomendado.add(headerPanel, c);
		
		// Adding button panel
		
		// GridBagLayout to allow for the whole button to be displayed
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);
		buttonPanel.add(makeUseRecomenadoButton());
		
		// To make this the right size
		buttonPanel.setPreferredSize(new Dimension(
				buttonPanel.getPreferredSize().width, 28));
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		panelRecomendado.add(buttonPanel, c);
		
		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades()) {
			
			if (i != null && !i.equals(Unidade.MIL�CIA)) {
				
				// Adds the JLabel
				JPanel unitQuantity = new JPanel();
				unitQuantity.setOpaque(false);
				
				unitQuantity.setPreferredSize(new Dimension(panelRecomendado.getPreferredSize().width, 30));
				
				mapRecomendado.put(i, new JLabel(" "));
				unitQuantity.add(mapRecomendado.get(i));
				
				// Changes the font color to gray
				mapRecomendado.get(i).setForeground(Color.gray);
				
				c.gridy++;
				panelRecomendado.add(unitQuantity, c);
				
			}
			
		} // ends for loop		
	}
	
	private JButton makeUseRecomenadoButton() {
		
		JButton button = new JButton("Usar");
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Map<Unidade, BigDecimal> recommended = c�lculo.getUnidadesRecomendadas();
				
				for (Entry<Unidade, IntegerFormattedTextField> entry : 
							panelUnidades.getTextFields().entrySet()) {
					
					if (!recommended.get(entry.getKey()).equals(BigDecimal.ZERO))
						entry.getValue().setText(recommended.get(entry.getKey()).toString());
					
				}
				
				panelRecomendado.setVisible(false);
				
			}
		});
		
		return button;
	}
	
	protected void setRecomendadoPanel(Map<Unidade, BigDecimal> map) {
		
		if (map != null) {
			panelRecomendado.setVisible(true);
			for (Unidade i : map.keySet())
				mapRecomendado.get(i).setText(map.get(i).toString());			
	
		} else {
			panelRecomendado.setVisible(false);
			for (Unidade i : mapRecomendado.keySet())
				mapRecomendado.get(i).setText(" ");
		}
		
	}
}