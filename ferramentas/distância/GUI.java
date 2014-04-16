package dist�ncia;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import config.Mundo_Reader;
import custom_components.Ferramenta;
import database.BigOperation;
import database.Cores;
import database.Unidade;

@SuppressWarnings("serial")
public class GUI extends Ferramenta {

	private List<PanelTempoUnidade> unidadesUtilizadas = new ArrayList<PanelTempoUnidade>();

	private PanelAldeia aldeiaOrigem, aldeiaDestino;

	private BigDecimal dist�ncia;

	/**
	 * Calcula o tempo de deslocamento entre duas aldeias. Mostra o tempo de
	 * cada unidade.
	 */
	public GUI() {

		super("C�lculo de Dist�ncia");

		setBackground(Cores.FUNDO_CLARO);
		setUnidades();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.gridx = 0;
		
		ActionListener action = new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				aldeiaOrigem.reset();
				aldeiaDestino.reset();
				
			}
		};
		
		constraints.anchor = GridBagConstraints.WEST;
		add(tools.addResetPanel(action), constraints);
		
		aldeiaOrigem = new PanelAldeia("Aldeia de Origem", this);

		constraints.gridy++;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.CENTER;
		add(aldeiaOrigem, constraints);

		aldeiaDestino = new PanelAldeia("Aldeia de Destino", this);

		constraints.gridx = 1;
		add(aldeiaDestino, constraints);

		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 0, 5);
		addHeader(constraints);

		constraints.gridy++;
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(unitePanels(), constraints);

	}

	/**
	 * Define as unidades que est�o presentes no mundo
	 */
	private void setUnidades() {

		for (Unidade i : Mundo_Reader.MundoSelecionado.getUnidades())
			if (i != null && !i.equals(Unidade.MIL�CIA))
				unidadesUtilizadas.add(new PanelTempoUnidade(getNextColor(), i));
	
	}

	protected void calculateDistanceAndTimes() {

		if (aldeiaOrigem.hasCompleteCoordinates()
				&& aldeiaDestino.hasCompleteCoordinates()) {

			int diferen�aX = aldeiaOrigem.getCoordenadaX()
					- aldeiaDestino.getCoordenadaX();
			int diferen�aY = aldeiaOrigem.getCoordenadaY()
					- aldeiaDestino.getCoordenadaY();

			BigDecimal xSquared = new BigDecimal(String.valueOf(diferen�aX))
					.pow(2);
			BigDecimal ySquared = new BigDecimal(String.valueOf(diferen�aY))
					.pow(2);

			dist�ncia = BigOperation.sqrt(xSquared.add(ySquared), 30);

			for (PanelTempoUnidade i : unidadesUtilizadas)
				i.setTempo(dist�ncia);

		} else
			for (PanelTempoUnidade i : unidadesUtilizadas)
				i.setTempo(null);

	}

	private void addHeader(GridBagConstraints c) {

		c.gridy++;
		add(new PanelTempoUnidade(), c);

	}

	/**
	 * Junta os panels de todas as unidades num �nico panel
	 * 
	 * @param String
	 *            qual panel pegar
	 */
	private JPanel unitePanels() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		panel.setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		for (PanelTempoUnidade i : unidadesUtilizadas) {
			panel.add(i, gbc);
			gbc.gridy++;
		}

		return panel;

	}

}
