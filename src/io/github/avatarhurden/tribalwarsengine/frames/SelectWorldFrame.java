package io.github.avatarhurden.tribalwarsengine.frames;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.listeners.TWEWindowListener;
import io.github.avatarhurden.tribalwarsengine.main.Configuration;
import io.github.avatarhurden.tribalwarsengine.panels.SelectWorldPanel;
import io.github.avatarhurden.tribalwarsengine.panels.WorldInfoPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.json.JSONObject;

import config.Lang;

public class SelectWorldFrame extends JFrame {

    public WorldInfoPanel informationTable;
    private SelectWorldPanel selectionPanel;

    private JPanel contentPanel;
    private static final SelectWorldFrame instance = new SelectWorldFrame();

    private final int MAX_WIDTH = 1024;
    private final int MAX_HEIGHT = 700;
    /**
     * Frame inicial, no qual ocorre a escolha do mundo. Ele possui:
     * - Logo do programa
     * - Tabela com as informa��es do mundo selecionado
     * - Lista dos mundos dispon�veis
     * - Bot�o para abrir o "MainWindow"
     */
    public SelectWorldFrame() {
    	setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
    	
        getContentPane().setBackground(Cores.ALTERNAR_ESCURO);
        setTitle(Lang.Titulo.toString());

        addWindowListener(new TWEWindowListener());

        setIconImage(Imagens.getImage("Icon.png"));
        
        setGUI();
        
        // Lista dos mundos com o bot�o para iniciar
        selectionPanel = new SelectWorldPanel(this);
    }

    public static SelectWorldFrame getInstance() {
        return instance;
    }
    
    private void setGUI() {
    	GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{600, 1, 393};
        layout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 0;

        c.gridwidth = 3;
        c.insets = new Insets(10, 5, 10, 5);
        add(makeLogoLabel(), c);

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 20, 5);
        add(contentPanel, c);

        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 5, 5);
        add(makeAuthorPane(), c);

        pack();
        setResizable(false);

        JSONObject location = Configuration.get().getConfig("location", new JSONObject());
        if (location.optInt("x", 0) < 0)
        	location.put("x", 0);
        if (location.optInt("y", 0) < 0)
        	location.put("y", 0);
        
        setLocation(location.optInt("x", 0), location.optInt("y", 0));
    }

    /**
     * Cria um JLabel com a imagem do logo, e adiciona no frame
     *
     * @param c GridBagConstraints para adicionar
     */
    private JLabel makeLogoLabel() {

        JLabel lblT�tulo = new JLabel("");

        /*
        * Irei criar um classe s� pra carregar os recursos de forma estatica,
        * Assim, poderemos manter o projeto mais organizado e mover todos os pacotes para dentro da SRC
        */
        lblT�tulo.setIcon(new ImageIcon(Imagens.getImage("logo_engine_centralized.png")));

       return lblT�tulo;
    }

    private JTextPane makeAuthorPane() {
    	JTextPane lblAuthor = new JTextPane();
    	lblAuthor.setEditable(false);
        lblAuthor.setOpaque(false);
        
        lblAuthor.setContentType("text/html");
        lblAuthor.setText(Lang.Criador.toString());
        
        return lblAuthor;
    }
    
    private JPanel makeSelectionPanel() {
    	JPanel panel = new JPanel();
        panel.setBackground(Cores.FUNDO_CLARO);
        panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{506, 1, 310};
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        
        // Tabela de informa��es
        informationTable = new WorldInfoPanel();
//        informationTable.changeProperties();
        
        c.gridy = 1;
        c.insets = new Insets(25, 5, 25, 5);
        panel.add(informationTable, c);

        c.gridx = 1;
        c.insets = new Insets(25, 0, 25, 0);

        JSeparator test = new JSeparator(SwingConstants.VERTICAL);
        test.setForeground(Cores.SEPARAR_ESCURO);
        c.fill = GridBagConstraints.VERTICAL;
        panel.add(test, c);

        c.gridx = 2;
        c.insets = new Insets(25, 5, 25, 5);
        
        panel.add(selectionPanel, c);
        
        return panel;
    }
    
    /**
     * Cria um JPanel com a tabela de informa��es do mundo, lista de mundos e
     * bot�o para iniciar e o adiciona no frame
     *
     * @param c GridBagConstraints para adicionar
     */
    public void addWorldPanel() {
    	
    	JPanel worldPanel = makeSelectionPanel();
    	
    	selectionPanel.setSelectionBox();
    	selectionPanel.changePadr�oButton();
    	
        contentPanel.removeAll();
        contentPanel.add(worldPanel);

        updateWorldInfoPanel();

        addKeyListener();
        repaint();
        selectionPanel.getComboBox().requestFocus();
    }
    
    private void addKeyListener() {
    	
    	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    	
    	manager.addKeyEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_PRESSED)
					selectionPanel.getStartButton().doClick();
				return false;
			}
		});
    	
    }
    
    /**
     * Muda as informa��es da tabela, chamado toda vez que o mundo selecionado �
     * alterado
     */
    public void updateWorldInfoPanel() {
        informationTable.changeProperties();
    }
    
    public void setInitializationPanel(JPanel panel) {
    	contentPanel.removeAll();
    	contentPanel.add(panel);
    	contentPanel.repaint();
    }

}
