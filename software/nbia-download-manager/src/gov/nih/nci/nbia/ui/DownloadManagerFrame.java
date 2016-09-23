/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.ui;

import gov.nih.nci.nbia.Application;
import gov.nih.nci.nbia.download.SeriesDownloaderFactory;
import gov.nih.nci.nbia.download.AbstractSeriesDownloader;
import gov.nih.nci.nbia.download.SeriesData;
import gov.nih.nci.nbia.util.ThreadPool;
import gov.nih.nci.nbia.util.PropertyLoader;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.nbia.util.SeriesComparitor;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author lethai
 *
 */
public class DownloadManagerFrame extends JFrame implements Observer {

    /* Download table's data model. */
    private DownloadsTableModel tableModel;
    private String errorText="An error has occurred. Please re-construct the data basket with un-downloaded image series.";

    /* Table showing downloads. */
    private JTable table;

    /*These are the buttons for managing the download. */
    public static JButton startButton;
    private JButton pauseButton, resumeButton;
    private JButton closeButton, deleteButton;
    public static JLabel errorLabel;

    /*Currently selected download. */
    private AbstractSeriesDownloader selectedDownload;
    private String userId="";
    private String password;
    private Integer noOfRetry;
    /*include annotation as part of the download if the series has annotation.*/
    private boolean includeAnnotation = true;

    private ThreadPool pool;

    /* Flag for whether or not table selection is being cleared. */
    private boolean clearing;
    private String serverUrl ="";
    private Integer maxThreads;

    private DirectoryBrowserPanel directoryBrowserPanel;

    public DownloadManagerFrame(String userId,
    		                    String password,
    		                    boolean includeAnnotation,
    		                    List<SeriesData> series,
    		                    String downloadServerUrl, Integer noOfRetry){
        this.userId = userId;
        this.includeAnnotation = includeAnnotation;
        this.noOfRetry = noOfRetry;
        this.errorText = "An error has occurred.";
        buildUI();
        PropertyLoader.loadProperties("config.properties");
        this.maxThreads = Application.getNumberOfMaxThreads();
        this.serverUrl = downloadServerUrl;
        this.password = password;
        Collections.sort(series, new SeriesComparitor());
        System.out.println("max threads: " + maxThreads + " serverurl " + serverUrl );
        try{
            addDownload(series);
        }catch(Exception e){
			e.printStackTrace();
            System.out.println("Error adding series to data table: " + e.getMessage());
        }
    }

    private void buildUI()
    {
        setTitle("NBIA Download Manager");
        setSize(800, 480);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });

        /* Set up file menu.*/
        createMenuBar();
        
        /* Set up browse panel.*/
        directoryBrowserPanel = new DirectoryBrowserPanel();

        /* Set up Downloads table.*/
        createTable();
        
        /* Set up buttons panel.*/
        JPanel buttonsPanel = createButtonsPanel();
        
        /* Set up items for the southern part of the UI.*/
        JPanel southPanel = new JPanel();    
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));       
        southPanel.add(directoryBrowserPanel);
        southPanel.add(buttonsPanel);
        
        /* Add panels to the display.*/               	         
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(createDownloadsPanel(), BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel createDownloadsPanel() {
        JPanel downloadsPanel = new JPanel();

        downloadsPanel.setBorder(
                 BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table),
                           BorderLayout.CENTER);
        return downloadsPanel;
    }

    private void createTable() {
        tableModel = new DownloadsTableModel();
        table = new DownloadsTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new
                ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row=table.rowAtPoint(e.getPoint());
                int col= table.columnAtPoint(e.getPoint());
                if (col == DownloadsTableModel.SERIES_ID_COLUMN + 4) {
                    JOptionPane.showMessageDialog(null," Detail status :- " +tableModel.getValueAt(row,DownloadsTableModel.SERIES_ID_COLUMN+5).toString());
                    }
                }
            });
    }
    private void createMenuBar(){
        JMenuBar menuBar = new MenuBar();
        setJMenuBar(menuBar);
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel();

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionStart();
            }
        });
        startButton.setEnabled(false);
        buttonsPanel.add(startButton);
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPause();
            }
        });
        pauseButton.setEnabled(false);
        buttonsPanel.add(pauseButton);
        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionResume();
            }
        });
        resumeButton.setEnabled(false);
        buttonsPanel.add(resumeButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionClear();
            }
        });
        deleteButton.setEnabled(false);
        buttonsPanel.add(deleteButton);

        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });
        closeButton.setEnabled(true);
        buttonsPanel.add(closeButton);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorLabel = new JLabel();
        errorLabel.setText(errorText);
        errorLabel.setVisible(false);
        errorPanel.add(errorLabel);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(errorPanel, BorderLayout.SOUTH);
        return panel;
    }
    private void actionExit() {
        System.exit(0);
    }

    private void addDownload(List<SeriesData> seriesData) throws Exception{
        Map<String, Integer> studyIdToSeriesCntMap = new HashMap<String, Integer>();

        for(int i=0; i< seriesData.size(); i++){
        	AbstractSeriesDownloader seriesDownloader = SeriesDownloaderFactory.createSeriesDownloader(seriesData.get(i).isLocal());

        	Integer seriesCnt = studyIdToSeriesCntMap.get(seriesData.get(i).getStudyInstanceUid());
        	if(seriesCnt==null) {
        		seriesCnt = 0;
        	}

        	seriesDownloader.start(serverUrl,
                                   seriesData.get(i).getCollection(),
                                   seriesData.get(i).getPatientId(),
                                   seriesData.get(i).getStudyInstanceUid(),
                                   seriesData.get(i).getSeriesInstanceUid(),
                                   this.includeAnnotation,
                                   seriesData.get(i).isHasAnnotation(),
                                   seriesData.get(i).getNumberImages(),
                                   this.userId,
                                   this.password,
                                   seriesData.get(i).getImagesSize(),
                                   seriesData.get(i).getAnnoSize(),
                                   
                                   StringUtil.displayAsSixDigitString(seriesCnt), noOfRetry);
            tableModel.addDownload(seriesDownloader);

            studyIdToSeriesCntMap.put(seriesData.get(i).getStudyInstanceUid(),
            		                  seriesCnt+1);
        }
    }

    /**
     * Grab the file location from the application for where to put downloaded files
     * and make sure each downloader is set to put files there.
     */
    private void setSeriesDownloadersOutputDirectory(String fileLocation) {
        File outputDir = null;
        if(fileLocation == null){
        	outputDir = new java.io.File(".");
        }
        else {
        	outputDir = new File(fileLocation);
        }

        for(int i =0;i<tableModel.getRowCount();i++) {
        	AbstractSeriesDownloader downloader = tableModel.getDownload(i);
        	downloader.setOutputDirectory(outputDir);
        }
    }

    /* Called when table row selection changes. */
    private void tableSelectionChanged() {
        /* Unregister from receiving notifications
        from the last selected download. */
        if (selectedDownload != null) {
            selectedDownload.deleteObserver(DownloadManagerFrame.this);
        }
        /* If not in the middle of clearing a download,
        set the selected download and register to
        receive notifications from it. */
        if (!clearing && table.getSelectedRow() > -1) {
            selectedDownload = tableModel.getDownload(table.getSelectedRow());
            selectedDownload.addObserver(DownloadManagerFrame.this);
            updateButtons();
        }
    }
    /* start the download. */
    private void actionStart(){
       startButton.setEnabled(false);
       setSeriesDownloadersOutputDirectory(this.directoryBrowserPanel.getDirectory());

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                pool = new ThreadPool(maxThreads);
                int size = tableModel.getRowCount();
                for (int i=0;i<size;i++) {
                    pool.assign(tableModel.getDownload(i));
                    tableModel.getDownload(i).addPropertyChangeListener(propertyChangeListener);
                }
                pool.addThreadPoolListener(new ButtonUpdater(pauseButton, resumeButton, errorLabel));  // lrt - changed to add errorLabel as a param
            }
        });
        pauseButton.setEnabled(true);
    }
    /* Pause the entire download. */
    private void actionPause() {
        int size = tableModel.getRowCount();
        for (int i=0;i<size;i++) {
        	AbstractSeriesDownloader sd = tableModel.getDownload(i);
            if(sd.getStatus() == AbstractSeriesDownloader.DOWNLOADING){
                sd.pause();
            }
        }
        pool.pause();
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(!pauseButton.isEnabled());
        System.out.println("Resume button enabled: " + resumeButton.isEnabled());
        //updateButtons();
    }
    /*Resume the entire download. */
    private void actionResume() {
        int size = tableModel.getRowCount();
        for (int i=0;i<size;i++) {
        	AbstractSeriesDownloader sd = tableModel.getDownload(i);
            if(sd.getStatus() == AbstractSeriesDownloader.PAUSED 
                    || sd.getStatus() == AbstractSeriesDownloader.NOT_STARTED){
                pool.assign(tableModel.getDownload(i));
            }

            if(sd.getStatus() == AbstractSeriesDownloader.PAUSED){
                sd.resume();
            }
        }
        resumeButton.setEnabled(false);
        pauseButton.setEnabled(!resumeButton.isEnabled());
        System.out.println("Pause button enabled: " + pauseButton.isEnabled());
        //updateButtons();
    }
    /* Clear the selected download.*/
    private void actionClear() {
        clearing = true;
        tableModel.clearDownload(table.getSelectedRow());
        clearing = false;
        selectedDownload = null;
        updateButtons();
    }
    /* Update each button's state based off of the
    currently selected download's status. */
    private void updateButtons(){
    	//this is invoked from a backgrouind thread as observer
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {

		        if (selectedDownload != null) {
		            int status = selectedDownload.getStatus();
		            switch (status) {
		            case AbstractSeriesDownloader.DOWNLOADING:
		                deleteButton.setEnabled(false);
		            break;
		            case AbstractSeriesDownloader.PAUSED:
		                deleteButton.setEnabled(false);
		                break;
		            case AbstractSeriesDownloader.ERROR:
		                deleteButton.setEnabled(true);
		                break;
		            default: // COMPLETE
		                deleteButton.setEnabled(true);
		            }
		        } else {
		            /*No download is selected in table.*/
		            deleteButton.setEnabled(false);
		        }
    		}
    	});
    }
    /* Update is called when a Download notifies its
    observers of any changes. */
    public void update(Observable o, Object arg) {
        /* Update buttons if the selected download has changed.	*/
        if (selectedDownload != null && selectedDownload.equals(o)) {
            updateButtons();
        }
    }

    private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            if("status".equals(evt.getPropertyName())){
                int status = Integer.parseInt(evt.getNewValue().toString());
                if(AbstractSeriesDownloader.ERROR == status && !errorLabel.isVisible()){
                    errorLabel.setVisible(true);
                }
            }
        }
    };
}