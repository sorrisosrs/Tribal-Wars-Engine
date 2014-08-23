package io.github.avatarhurden.tribalwarsengine.objects;

import io.github.avatarhurden.tribalwarsengine.components.IntegerFormattedTextField;
import io.github.avatarhurden.tribalwarsengine.components.TroopLevelComboBox;
import io.github.avatarhurden.tribalwarsengine.managers.WorldManager;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import database.Cores;
import database.ItemPaladino;
import database.Unidade;
import database.Unidade.UnidadeTipo;

/**
 * Essa classe guarda informa��es relativas a um ex�rcito. Ela cont�m
 * as tropas dispon�veis e as quantidades, e pode fornecer informa��es como ataque,
 * defesa, popula��o etc
 * @author Arthur
 *
 */
public class Army {
	
	private List<Tropa> tropas = new ArrayList<Tropa>();
	
	public Army() {
		this(Arrays.asList(Unidade.values()));
	}
	
	public Army(List<Unidade> units) {
		
		for (Unidade i : units)
			tropas.add(new Tropa(i, 0));
		
	}
	
	/**
	 * Adiciona uma nova tropa ao ex�rcito. Se j� existe uma tropa com a
	 * unidade especificada, ela ser� removida
	 * 
	 * @param unidade
	 * @param quantidade
	 * @param nivel
	 */
	public void addTropa(Unidade unidade, int quantidade, int nivel) {
		
		Iterator<Tropa> iter = tropas.iterator();
		while (iter.hasNext())
			if (iter.next().unidade.equals(unidade))
				iter.remove();
		
		tropas.add(new Tropa(unidade, quantidade, nivel, nivel));
		
	}
	
	/**
	 * Adiciona uma nova tropa ao ex�rcito. Se j� existe uma tropa com a
	 * unidade especificada, ela ser� removida
	 * 
	 * @param unidade
	 * @param quantidade
	 * @param nivel3 - N�vel da Unidade em um mundo com pesquisa de 3 n�veis
	 * @param nivel10 - N�vel da Unidade em um mundo com pesquisa cl�ssica
	 */
	public void addTropa(Unidade unidade, int quantidade, int nivel3, int nivel10) {
		addTropa(new Tropa(unidade, quantidade, nivel3, nivel10));
	}
	
	public void addTropa(Tropa tropa) {
		int position = tropas.size()-1;
		
		Iterator<Tropa> iter = tropas.iterator();
		while (iter.hasNext())
			if (iter.next().unidade.equals(tropa.unidade)) {
				position = tropas.indexOf(getTropa(tropa.unidade));
				iter.remove();
			}
		
		tropas.add(position, tropa);
	}
	
	public List<Tropa> getTropas() {
		return tropas;
	}
	
	public List<Unidade> getUnidades() {
		List<Unidade> list = new ArrayList<>();
		for (Tropa t : tropas)
			list.add(t.unidade);
		
		return list;
	}

	public int getQuantidade(Unidade i) {
		return getTropa(i).quantidade;
	}
	
	public Tropa getTropa(Unidade u) {
		for (Tropa t : tropas)
			if (t.unidade.equals(u))
				return t;
		
		return null;
	}
	
	public boolean contains(Unidade u) {
		for (Tropa t : tropas)
			if (t.unidade.equals(u))
				return true;
		
		return false;
	}
	
	public int getAtaque(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units.length == 0)
			for (Tropa t : tropas)
				ataque += t.getAtaque(item);
		else
			for (Unidade u : units)
				ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaque(Unidade... units) {
		return getAtaque(ItemPaladino.NULL, units);
	}
	
	public int getAtaqueGeral(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Geral))
					ataque += t.getAtaque(item);
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Geral))
					ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueGeral(Unidade... units) {
		return getAtaqueGeral(ItemPaladino.NULL, units);
	}
	
	public int getAtaqueCavalaria(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Cavalo))
					ataque += t.getAtaque(item);
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Cavalo))
					ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueCavalaria(Unidade... units) {
		return getAtaqueCavalaria(ItemPaladino.NULL, units);
	}
	
	public int getAtaqueArqueiro(ItemPaladino item, Unidade... units) {
		int ataque = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				if (t.getTipo().equals(UnidadeTipo.Arqueiro))
					ataque += t.getAtaque(item);
		} else
			for (Unidade u : units)
				if (getTropa(u).getTipo().equals(UnidadeTipo.Arqueiro))
					ataque += getTropa(u).getAtaque(item);
		
		return ataque;
	}
	
	public int getAtaqueArqueiro(Unidade... units) {
		return getAtaqueArqueiro(ItemPaladino.NULL, units);
	}
	
	public int getDefesaGeral(ItemPaladino item, Unidade... units) {
		int defesa = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				defesa += t.getDefesaGeral(item);
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaGeral(item);
		
		return defesa;
	}
	
	public int getDefesaGeral(Unidade... units) {
		return getDefesaGeral(ItemPaladino.NULL, units);
	}
	
	public int getDefesaCavalaria(ItemPaladino item, Unidade... units) {
		int defesa = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				defesa += t.getDefesaCavalaria(item);
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaCavalaria(item);
		
		return defesa;
	}
	
	public int getDefesaCavalaria(Unidade... units) {
		return getDefesaCavalaria(ItemPaladino.NULL, units);
	}
	
	public int getDefesaArqueiro(ItemPaladino item, Unidade... units) {
		int defesa = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				defesa += t.getDefesaArqueiro(item);
		} else
			for (Unidade u : units)
				defesa += getTropa(u).getDefesaArqueiro(item);
		
		return defesa;
	}
	
	public int getDefesaArqueiro(Unidade... units) {
		return getDefesaArqueiro(ItemPaladino.NULL, units);
	}
	
	public int getSaque(Unidade... units) {
		int saque = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				saque += t.getSaque();
		} else
			for (Unidade u : units)
				saque += getTropa(u).getSaque();
		
		return saque;
	}
	
	public int getPopula��o(Unidade... units) {
		int popula��o = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				popula��o += t.getPopula��o();
		} else
			for (Unidade u : units)
				popula��o += getTropa(u).getPopula��o();
		
		return popula��o;
	}
	
	public int getCustoMadeira(Unidade... units) {
		int madeira = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				madeira += t.getCustoMadeira();
		} else
			for (Unidade u : units)
				madeira += getTropa(u).getCustoMadeira();
		
		return madeira;
	}
	
	public int getCustoArgila(Unidade... units) {
		int argila = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				argila += t.getCustoArgila();
		} else
			for (Unidade u : units)
				argila += getTropa(u).getCustoArgila();
		
		return argila;
	}
	
	public int getCustoFerro(Unidade... units) {
		int ferro = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				ferro += t.getCustoFerro();
		} else
			for (Unidade u : units)
				ferro += getTropa(u).getCustoFerro();
		
		return ferro;
	}
	
	/**
	 * Retorna a "velocidade" da unidade mais lenta do ex�rcito, em milissegundos/campo.
	 * S�o consideradas a velocidade e modificador de unidade do mundo.
	 */
	public double getVelocidade(Unidade... units) {
		
		double slowest = 0;
		
		if (units.length == 0) {
			for (Tropa t : tropas)
				if (t.getVelocidade() > slowest)
					slowest = t.getVelocidade();
		} else 
			for (Unidade u : units)
				if (getTropa(u).getVelocidade() > slowest)
					slowest = getTropa(u).getVelocidade();
	
		// Transforma de minutos/campo para milissegundos/campo
		return slowest*60000;
	}
	
	/**
	 * Classe que representa uma tropa espec�fica, com a unidade, n�vel e quantidade
	 * @author Arthur
	 *
	 */
	public class Tropa {
		
		private Unidade unidade;
		private int quantidade;
		private int nivel3;
		private int nivel10;
		
		private transient int nivel;
		
		private Tropa(Unidade unidade, int quantidade, int nivel3, int nivel10) {
			this.unidade = unidade;
			this.quantidade = quantidade;
			this.nivel3 = nivel3;
			this.nivel10 = nivel10;
			
			if (WorldManager.get().getSelectedWorld().getResearchSystem().getResearch() == 10)
				nivel = nivel10;
			else if (WorldManager.get().getSelectedWorld().getResearchSystem().getResearch() == 3)
				nivel = nivel3;
			else
				nivel = 1;
		}
		
		private Tropa(Unidade unidade, int quantidade) {
			this(unidade, quantidade, 1, 1);
		}
		
		private int getAtaque(ItemPaladino item) {
			return unidade.getAtaque(nivel, item) * quantidade;
		}
		
		private int getDefesaGeral(ItemPaladino item) {
			return unidade.getDefGeral(nivel, item) * quantidade;
		}
		
		private int getDefesaCavalaria(ItemPaladino item) {
			return unidade.getDefCav(nivel, item) * quantidade;
		}
		
		private int getDefesaArqueiro(ItemPaladino item) {
			return unidade.getDefArq(nivel, item) * quantidade;
		}
		
		private int getCustoMadeira() {
			return unidade.getMadeira() * quantidade;
		}
		
		private int getCustoArgila() {
			return unidade.getArgila() * quantidade;
		}
		
		private int getCustoFerro() {
			return unidade.getFerro() * quantidade;
		}
		
		private int getPopula��o() {
			return unidade.getPopula��o() * quantidade;
		}
		
		private int getSaque() {
			return unidade.getSaque() * quantidade;
		}
		
		private int getTempoProdu��o() {
			return unidade.getTempoDeProdu��o() * quantidade * 1000;
		}
		
		private double getVelocidade() {
			if (quantidade == 0)
				return 0;
			else
				return unidade.getVelocidade()
						* WorldManager.get().getSelectedWorld().getWorldSpeed()
						* WorldManager.get().getSelectedWorld().getUnitModifier();
		}
		
		private UnidadeTipo getTipo() {
			return unidade.getType();
		}
		
		public Unidade getUnidade() {
			return unidade;
		}
		
		public int getQuantidade() {
			return quantidade;
		}

	}
	
	public static boolean isArmyJson(JSONObject json) {
		return json.has("tropas");
		
	}
	
	public static ArrayList<Unidade> getAvailableUnits() {
		
		ArrayList<Unidade> list = new ArrayList<Unidade>();
    	
    	for (Unidade u : Unidade.values())
    		list.add(u);
    	
    	if (!WorldManager.get().getSelectedWorld().isArcherWorld()) {
    		list.remove(Unidade.ARQUEIRO);
    		list.remove(Unidade.ARCOCAVALO);
    	}
    	if (!WorldManager.get().getSelectedWorld().isPaladinWorld())
    		list.remove(Unidade.PALADINO);
    	if (!WorldManager.get().getSelectedWorld().isMilitiaWorld())
    		list.remove(Unidade.MIL�CIA);
    	
    	return list;
		
	}
	
	public static ArrayList<Unidade> getAttackingUnits() {
		
		ArrayList<Unidade> list = getAvailableUnits();
    	
    	list.remove(Unidade.MIL�CIA);
    	
    	return list;
		
	}
	
	@SuppressWarnings("serial")
	public class ArmyEditPanel extends JPanel {
		
		private HashMap<Unidade, IntegerFormattedTextField> quantities;
		private HashMap<Unidade, TroopLevelComboBox> level3;
		private HashMap<Unidade, TroopLevelComboBox> level10;
		
		private GridBagLayout layout;
		
		private boolean hasHeader, hasNames, hasAmount, hasNivel3, hasNivel10;
		private OnChange onChange;
		
		/**
		 * Cria um JPanel para edi��o de um Army. � poss�vel escolher quais 
		 * componentes ele possui. <p>Caso <code>hasHeader</code> seja <code>false</code>,
		 * o resto do JPanel n�o possui uma borda. Caso seja <code>true</code>,
		 * o header fica localizado a 5px acima das informa��es, com uma borda em
		 * volta de cada um. <p>Para que todas as unidades possam ser modificadas
		 * pelo JPanel, deixar <code>unidades</code> em branco.
		 * 
		 * @param onChange
		 * @param hasHeader
		 * @param hasNames
		 * @param hasAmount
		 * @param hasNivel3
		 * @param hasNivel10
		 * @param unidades
		 */
		public ArmyEditPanel(OnChange onChange, boolean hasHeader, 
				boolean hasNames, boolean hasAmount,
				boolean hasNivel3, boolean hasNivel10, Unidade... unidades) {
			
			quantities = new HashMap<Unidade, IntegerFormattedTextField>();
			level3 = new HashMap<Unidade, TroopLevelComboBox>();
			level10 = new HashMap<Unidade, TroopLevelComboBox>();
			
			this.hasHeader = hasHeader;
			this.hasNames = hasNames;
			this.hasAmount = hasAmount;
			this.hasNivel3 = hasNivel3;
			this.hasNivel10 = hasNivel10;
			this.onChange = onChange;
			
			setLayout();
			
			setOpaque(false);
			
	        setLayout(new GridBagLayout());
	        
	        GridBagConstraints c = new GridBagConstraints();
	        c.gridx = 0;
			c.gridy = 0;
	        c.gridwidth = 1;
	        c.anchor = GridBagConstraints.EAST;
	        c.fill = GridBagConstraints.BOTH;
			
	        if (hasHeader) {
	        	c.insets = new Insets(0, 0, 5, 0);
	        	add(headerPanel(), c);
	        	c.gridy++;
	        }
	        
	        c.insets = new Insets(0, 0, 0, 0);
	        add(mainPanel(unidades), c);
		}
		
		private void setLayout() {
			
			ArrayList<Integer> widths = new ArrayList<Integer>();
			if (hasNames)
				widths.add(125);
			if (hasAmount)
				widths.add(100);
			if (hasNivel3)
				widths.add(40);
			if (hasNivel10)
				widths.add(40);
			
			int[] widthsArray = new int[widths.size()];
			
			for (int i = 0; i < widthsArray.length; i++)
				widthsArray[i] = widths.get(i);
			
			layout = new GridBagLayout();
		    layout.columnWidths = widthsArray;
		    layout.rowHeights = new int[]{20};
		    layout.columnWeights = new double[]{1, Double.MIN_VALUE};
		    layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
			
		}
		
		private JPanel headerPanel() {
			
			JPanel panel = new JPanel();
			panel.setBackground(Cores.FUNDO_ESCURO);
			panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			GridBagLayout layout = this.layout;
			layout.rowHeights = new int[] {25};
			panel.setLayout(layout);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.CENTER;
			
			if (hasNames)
				panel.add(new JLabel("Unidade"), c);
			
			if (hasAmount) {
				c.gridx++;
				panel.add(new JLabel("Quantidade"), c);
			}
			
			if (hasNivel3 && hasNivel10) {
				c.gridx++;
				panel.add(new JLabel("<html><center>N�vel<br>(3)</center></html>"), c);
				c.gridx++;
				panel.add(new JLabel("<html><center>N�vel<br>(10)</center></html>"), c);
			} else {
				if (hasNivel3) {
					c.gridx++;
					panel.add(new JLabel("N�vel"), c);
				}
			
				if (hasNivel10) {
					c.gridx++;
					panel.add(new JLabel("N�vel"), c);
				}
			}
			
			return panel;
		}
		
		private JPanel mainPanel(Unidade... unidades) {
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setOpaque(false);
			if (hasHeader)
				panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
			
			Unidade[] values = unidades;
			if (values.length == 0) {
				values = new Unidade[tropas.size()];
				for (int i = 0; i < tropas.size(); i++)
					values[i] = tropas.get(i).unidade;
			}
			
			for (int i = 0; i < values.length; i++) {
				
				Unidade u = values[i];
				
				JPanel unitPanel = new JPanel(layout);
				unitPanel.setBackground(Cores.getAlternar(i));
				
				GridBagConstraints panelC = new GridBagConstraints();
				panelC.insets = new Insets(5, 5, 5, 5);
				panelC.gridx = 0;
				panelC.gridy = 0;
				panelC.fill = GridBagConstraints.BOTH;
				
				JLabel label = new JLabel(u.toString());
				label.setHorizontalAlignment(SwingConstants.LEADING);
				if (hasNames) {
					unitPanel.add(label, panelC);
					panelC.gridx++;
				}
				
				IntegerFormattedTextField txt = new IntegerFormattedTextField(getQuantidade(u)) {
					public void go() {
						onChange.run();
					}
				};
				
				quantities.put(u, txt);
	
				if (hasAmount) {
					unitPanel.add(txt, panelC);
					panelC.gridx++;
				}
				
				TroopLevelComboBox combo3 = new TroopLevelComboBox(3, unitPanel.getBackground());
				combo3.setSelectedItem(Army.this.getTropa(u).nivel3);
				level3.put(u, combo3);
					
				combo3.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (hasNivel3) {
					unitPanel.add(combo3, panelC);
					panelC.gridx++;
				}
				
				TroopLevelComboBox combo10 = new TroopLevelComboBox(10, unitPanel.getBackground());
				combo10.setSelectedItem(Army.this.getTropa(u).nivel10);
				level10.put(u, combo10);
					
				combo10.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						onChange.run();
					}
				});
				
				if (hasNivel10){
					unitPanel.add(combo10, panelC);
					panelC.gridx++;
				}
				
				panel.add(unitPanel);
			}
			
			return panel;
			
		}
		
		public void saveValues() {
			
			for (Unidade i : quantities.keySet())
				Army.this.addTropa(i, quantities.get(i).getValue().intValue(),
						(int) level3.get(i).getSelectedItem(),
						(int) level10.get(i).getSelectedItem() );
		}
		
		public void resetComponents() {
			
			for (IntegerFormattedTextField t : quantities.values())
				t.setText("");
			for (TroopLevelComboBox t : level3.values())
				t.setSelectedIndex(0);
			for (TroopLevelComboBox t : level10.values())
				t.setSelectedIndex(0);
			
		}
		
		public void setValues(Army army) {
			
			resetComponents();
			
			for (Tropa t : army.tropas) {
				if (t.quantidade > 0)
					quantities.get(t.unidade).setText(String.valueOf(t.quantidade));	
				level3.get(t.unidade).setSelectedItem(t.nivel3);
				level10.get(t.unidade).setSelectedItem(t.nivel10);
			}
			
			saveValues();
			
		}
		
		public Army getArmy() {
			return Army.this;
		}
		
	}
	
}