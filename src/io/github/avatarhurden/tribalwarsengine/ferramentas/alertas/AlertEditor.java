package io.github.avatarhurden.tribalwarsengine.ferramentas.alertas;

import io.github.avatarhurden.tribalwarsengine.components.CoordenadaPanel;
import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TWEComboBox;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Aldeia;
import io.github.avatarhurden.tribalwarsengine.ferramentas.alertas.Alert.Tipo;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.objects.World;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army;
import io.github.avatarhurden.tribalwarsengine.objects.unit.Army.ArmyEditPanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

/**
 * Classe para criar ou editar objetos da classe Alert
 * 
 * @author Arthur
 */
public class AlertEditor extends JDialog{
	
	// Necess�rio para poder cancelar modifica��es
	private Alert alerta;
	
	// Nome do alerta
	private JTextField nome;
	
	// Data do alerta
	private JSpinner spinnerDate, spinnerHour;
	
	// Repeti��o do alerta
	private JSpinner spinnerRepeat;
	private IntegerFormattedTextField textRepeat;
	
	// Qual o tipo do alerta
	private JPanel[] tipos;
	
	private TWEComboBox<World> worldSelector;
	
	// Aldeia de origem
	private CoordenadaPanel origemCoord;
	private JTextField origemNome;
	
	// Aldeia de destino
	private CoordenadaPanel destinoCoord;
	private JTextField destinoNome;
	
	// Tropas enviadas
	private ArmyEditPanel armyEdit;
	
	// Notas relacionadas ao alerta
	private JTextArea notas;
	
	// Per�odos de avisos antes do evento
	private JButton addAviso;
	private LinkedHashMap<IntegerFormattedTextField, JComboBox<String>> avisos;
	
	// Componentes desativados quando o aviso � do tipo geral
	private List<Container> villageComponents = new ArrayList<Container>();
	
	// Scrollpane
	private JScrollPane scroll;
	
	/**
	 * Cria um editor em branco.
	 */
	public AlertEditor() {
		
		setResizable(false);
		
		setLayout(new GridBagLayout());
		getContentPane().setBackground(Cores.FUNDO_CLARO);
		
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(Cores.FUNDO_CLARO);
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		
		// Adding name panel
		panel.add(makeNamePanel(), c);
		
		c.gridy++;
		panel.add(makeTipoPanel(), c);

		c.gridy++;
		panel.add(makeWorldPanel(), c);
		
		c.gridy++;
		panel.add(makeDataPanel(), c);
		
		c.gridy++;
		panel.add(makeRepetePanel(), c);
		
		c.gridwidth = 1;
		c.gridy++;
		panel.add(makeOrigemPanel(), c);
		
		c.gridx++;
		panel.add(makeDestinoPanel(), c);

		c.gridwidth = 2;
		c.gridy++;
		c.gridx--;
		panel.add(makeTropaPanel(), c);
		
		c.gridy++;
		panel.add(makeNotasPanel(null), c);
		
		c.gridy++;
		panel.add(makeAvisosPanel(), c);
		
		scroll = new JScrollPane(panel);
		scroll.setOpaque(false);
		scroll.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		scroll.setPreferredSize(new Dimension(scroll.getPreferredSize().width+30, 500));
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 0;
		c.gridx = 0;
		add(scroll, c);
		
		c.gridy++;
		add(makeButtons(), c);

		// Como o tipo selecionado � geral, desativa as coisas adequadas.
		for (Container t : villageComponents)
			enableComponents(t, false);

		pack();
		setLocationRelativeTo(null);
		
	}
	
	/**
	 * Cria um editor com dados preenchidos, baseado no alerta passado como par�metro
	 * @param alerta com o qual preencher os dados
	 */
	@SuppressWarnings("unchecked")
	public AlertEditor(Alert alerta) {
		
		this();
		
		this.alerta = alerta;
		
		String stringNome = alerta.getNome();
		if (stringNome != null) 
			nome.setText(stringNome);
		
		Date hora = alerta.getHor�rio();
		if (hora != null) {
			spinnerDate.setValue(hora);
			spinnerHour.setValue(hora);
		}
		
		Long repeat = alerta.getRepete();
		if (repeat != null) {
			textRepeat.setText(String.valueOf(repeat / (24*60*60*1000)));
			spinnerRepeat.setValue(new Date(3*60*60*1000 + repeat));
			// Constantes para zerar as horas, visto que Date come�a �s 21:00
		}
		
		Tipo tipo = alerta.getTipo();
		if (tipo != null) 
			switch (tipo) {
			case Geral : tipos[0].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[0], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			case Ataque: tipos[1].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[1], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			case Apoio: tipos[2].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[2], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			case Saque: tipos[3].getMouseListeners()[0].mouseClicked(
					new MouseEvent(tipos[3], 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
				break;
			}
		
		World world = alerta.getWorld();
		if (world != null)
			worldSelector.setSelectedItem(world);
		
		Aldeia origem = alerta.getOrigem();
		if (origem != null) {
			origemCoord.setCoordenadas(origem.x, origem.y);
			origemNome.setText(origem.nome);
		}
		
		Aldeia destino = alerta.getDestino();
		if (destino != null) {
			destinoCoord.setCoordenadas(destino.x, destino.y);
			destinoNome.setText(destino.nome);
		}
		
		Army army = alerta.getArmy();
		if (army != null)
			armyEdit.setValues(army);
		
		String stringNotas = alerta.getNotas();
		if (stringNotas != null) 
			notas.setText(stringNotas);
		
		Stack<Date> stackAvisos = alerta.getAvisos();
		if (stackAvisos != null)
			for (Date d : stackAvisos) {
				long tempo = hora.getTime()-d.getTime();
				
				addAviso.doClick();
				
				// Aviso � x horas antes
				if (tempo % (60*60*1000) == 0) {
					((JTextField) avisos.keySet().toArray()[avisos.size()-1]).setText(""+tempo/(60*60*1000));
					((JComboBox<String>) avisos.values().toArray()[avisos.size()-1]).setSelectedIndex(0);
				} else if (tempo % (60*1000) == 0) {
					((JTextField) avisos.keySet().toArray()[avisos.size()-1]).setText(""+tempo/(60*1000));
					((JComboBox<String>) avisos.values().toArray()[avisos.size()-1]).setSelectedIndex(1);
				} else if (tempo % 1000 == 0) {
					((JTextField) avisos.keySet().toArray()[avisos.size()-1]).setText(""+tempo/(1000));
					((JComboBox<String>) avisos.values().toArray()[avisos.size()-1]).setSelectedIndex(2);
				}
				
			}
		
		scroll.getVerticalScrollBar().setValue(0);
		
		// Permite a atualiza��o do Caret nas notas
		((DefaultCaret)notas.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	/**
	 * Define o alerta com base no que est� preenchido no editor.
	 */
	protected void setAlerta() {
		
		if (alerta == null)
			alerta = new Alert();
		
		alerta.setNome(nome.getText());
		
		long time;
		// Gets the dateSpinner part of the time
		time = TimeUnit.MILLISECONDS.toDays(((Date) spinnerDate.getModel().getValue()).getTime());
		time = TimeUnit.DAYS.toMillis(time);
		// Gets the hours part of the time                  days*minutes*millis
		time += ((Date) spinnerHour.getModel().getValue()).getTime()%(24*3600*1000);

		alerta.setHor�rio(new Date(time));
		
		long repeat;
		repeat = ((Date) spinnerRepeat.getModel().getValue()).getTime();
		repeat -= 3*60*60*1000;
		repeat += textRepeat.getValue() * 24*60*60*1000;
		alerta.setRepete(repeat);
		
		for (int i = 0; i < 4; i++)
			if (tipos[i].getBackground().equals(Cores.FUNDO_ESCURO))
				alerta.setTipo(Tipo.values()[i]);
		
		alerta.setWorld((World) worldSelector.getSelectedItem());
		
		alerta.setOrigem(new Aldeia(origemNome.getText(), origemCoord.getCoordenadaX(), origemCoord.getCoordenadaY()));
		
		alerta.setDestino(new Aldeia(destinoNome.getText(), destinoCoord.getCoordenadaX(), destinoCoord.getCoordenadaY()));
		
		armyEdit.saveValues();
		alerta.setArmy(armyEdit.getArmy());
		
		alerta.setNotas(notas.getText());
		
		List<Date> lista = new ArrayList<Date>();
		
		for (Entry<IntegerFormattedTextField, JComboBox<String>> e : avisos.entrySet()) {
			
			long entry = e.getKey().getValue();
			
			entry *= 1000*(Math.pow(60,Math.abs(2-e.getValue().getSelectedIndex())));
			
			lista.add(new Date(alerta.getHor�rio().getTime() - entry));
		}
			
		alerta.setAvisos(lista);
		
		if (this.alerta == null || this.alerta.getRepete() == null)
			alerta.setRepete(0);
		else
			alerta.setRepete(this.alerta.getRepete());
	}
	
	/**
	 * Cria um JPanel para inserir o nome do alerta
	 */
	private JPanel makeNamePanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Nome"));
		
		nome = new JTextField(16);
		panel.add(nome);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir o tipo do alerta
	 */
	private JPanel makeTipoPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Tipo"));
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		
		JPanel geral = new JPanel();
		JPanel ataque = new JPanel();
		JPanel apoio = new JPanel();
		JPanel saque = new JPanel();
		
		geral.add(new JLabel("Geral"));
		ataque.add(new JLabel("Ataque"));
		apoio.add(new JLabel("Apoio"));
		saque.add(new JLabel("Saque"));
		
		tipos = new JPanel[] {geral, ataque, apoio, saque};
		
		for (JPanel p : tipos) {
			p.setBackground(Cores.FUNDO_CLARO);
			p.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		}
		
		tipos[0].setBackground(Cores.FUNDO_ESCURO);
		tipos[0].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		
		MouseAdapter listener = new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				
				for (JPanel p : tipos) {
					p.setBackground(Cores.FUNDO_CLARO);
					p.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
				}
					
				((JPanel) e.getSource()).setBackground(Cores.FUNDO_ESCURO);
				((JPanel) e.getSource()).setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				
				if (((JPanel) e.getSource()).equals(tipos[0]))
					for (Container c : villageComponents)
						enableComponents(c, false);
				else
					for (Container c : villageComponents)
						enableComponents(c, true);
				
			}
		};
		
		for (int i = 0; i < tipos.length; i++) {
			tipos[i].addMouseListener(listener);
			
			if (i == 1 || i == 2) c.insets = new Insets(5, 0, 5, 0);
			else if (i == tipos.length-1) c.insets = new Insets(5, 0, 5, 5);
			c.gridx++;
			panel.add(tipos[i], c);
		}
		
		return panel;
	}
	
	private JPanel makeWorldPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Mundo"));

		worldSelector = new TWEComboBox<World>();
		
		for (World w : WorldManager.get().getList())
			worldSelector.addItem(w);
	
		worldSelector.setSelectedItem(WorldManager.getSelectedWorld());
	
		panel.add(worldSelector);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir o hor�rio do alerta
	 */
	private JPanel makeDataPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Data"));
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		spinnerDate = new JSpinner(new SpinnerDateModel());
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "dd/MM/yyy"));
		
		spinnerHour = new JSpinner(new SpinnerDateModel());
		spinnerHour.setEditor(new JSpinner.DateEditor(spinnerHour, "HH:mm:ss"));
		
		panel.add(spinnerHour, c);
		
		c.gridx++;
		panel.add(spinnerDate, c);
		
		return panel;
	}
	
	private JPanel makeRepetePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Intervalo"));
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;

		textRepeat = new IntegerFormattedTextField(0, 3, null);
		textRepeat.setHorizontalAlignment(SwingConstants.TRAILING);
		
		Date min = new Date(3*60*60000);
		Date max = new Date(27*60*60000);
		
		spinnerRepeat = new JSpinner(new SpinnerDateModel(min, min, max, 0));
		spinnerRepeat.setEditor(new JSpinner.DateEditor(spinnerRepeat, "HH:mm:ss"));
		
		panel.add(textRepeat, c);
		
		c.gridx++;
		panel.add(new JLabel("dias"), c);
		
		c.gridy++;
		panel.add(spinnerRepeat);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir a origem do alerta. Fica desativado caso o alerta seja de tipo Geral
	 */
	private JPanel makeOrigemPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Origem"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 0, 0, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
	
		origemCoord = new CoordenadaPanel(null, null);
		origemCoord.setBorder(new EmptyBorder(0, 0, 0, 0));
		origemCoord.setOpaque(false);
		
		panel.add(origemCoord, c);
		
		origemNome = new JTextField(9);
		
		c.insets = new Insets(0, 5, 5, 0);
		c.gridy++;
		panel.add(origemNome, c);
		
		villageComponents.add(panel);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir o destino do alerta. Fica desativado caso o alerta seja de tipo Geral
	 */
	private JPanel makeDestinoPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Destino"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 0, 0, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		destinoCoord = new CoordenadaPanel(null, null);
		destinoCoord.setBorder(new EmptyBorder(0, 0, 0, 0));
		destinoCoord.setOpaque(false);
		
		panel.add(destinoCoord, c);
		
		destinoNome = new JTextField(9);

		c.insets = new Insets(0, 5, 5, 0);
		c.gridy++;
		panel.add(destinoNome, c);

		villageComponents.add(panel);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir as tropas enviadas do alerta. Fica desativado caso o alerta seja de tipo Geral
	 */
	private JPanel makeTropaPanel() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Tropas"));
		
		armyEdit = new Army(Army.getAttackingUnits())
			.getEditPanelNoLevelsNoHeader(null, 30);
		
		armyEdit.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
		panel.add(armyEdit);
		
		villageComponents.add(armyEdit);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir as notas do alerta
	 */
	private JPanel makeNotasPanel(String nota) {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Notas"));
		
		notas = new JTextArea(nota, 5, 20);
		// N�o permite que o textArea modifique a posi��o do scrollpane
		((DefaultCaret)notas.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		notas.setBorder(new LineBorder(Cores.SEPARAR_CLARO));
		notas.setLineWrap(true);
		notas.setWrapStyleWord(true);
		
		panel.add(notas);
		
		return panel;
	}
	
	/**
	 * Cria um JPanel para definir os avisos pr�vios do alerta. Cada aviso � composto de um n�mero e uma
	 * unidade (horas, minutos segundos). � poss�vel adicionar novos avisos clicando no bot�o '+'.
	 */
	private JPanel makeAvisosPanel() {
		
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(new TitledBorder(new LineBorder(Cores.SEPARAR_ESCURO), "Avisos"));
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		
		avisos = new LinkedHashMap<IntegerFormattedTextField, JComboBox<String>>();
		
		addAviso = new TWSimpleButton("+");
		addAviso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				IntegerFormattedTextField amount = new IntegerFormattedTextField(0, 4, null);
				
				JComboBox<String> unit = new JComboBox<String>(new String[] { "horas", "minutos", "segundos" });
				
				avisos.put(amount, unit);	
				
				panel.remove((Component) e.getSource());
				
				JPanel avisoPanel = new JPanel();
				avisoPanel.setOpaque(false);
				avisoPanel.add(amount);
				avisoPanel.add(unit);
				avisoPanel.add(new JLabel("antes"));
				
				panel.add(avisoPanel, c);
				
				c.gridy++;
				panel.add((Component) e.getSource(), c);
				
				revalidate();
				pack();
				
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
				
			}
		});
		
		panel.add(addAviso, c);
		
		return panel;
	}
	
	/**
	 * Cria os bot�es que ficam na parte inferior do editor (salvar e cancelar).
	 */
	private JPanel makeButtons() {
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		JButton salvar = new TWSimpleButton("Salvar");
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				setAlerta();
				dispose();
				
			}
		});
		
		JButton cancelar = new TWSimpleButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alerta = null;
				dispose();
			}
		});
		
		panel.add(salvar);
		panel.add(cancelar);
		
		return panel;
	}
	
	/**
	 * Retorna o alerta definido pelo editor
	 * @return alerta
	 */
	public Alert getAlerta() {
		return alerta;
	}
	
	public void enableComponents(Container container, boolean isEnabled) {
		 Component[] components = container.getComponents();
	        for (Component component : components) {
	            component.setEnabled(isEnabled);
	            if (component instanceof Container)
	                enableComponents((Container)component, isEnabled);
	        }
	}
	
}
