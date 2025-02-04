/* 
 * This file is part of Quelea, free projection software for churches.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.quelea.windows.main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import org.quelea.services.utils.LoggerUtils;
import org.quelea.services.utils.QueleaProperties;
import org.quelea.windows.library.LibraryPanel;
import org.quelea.windows.main.schedule.SchedulePanel;

/**
 * The main body of the main window, containing the schedule, the media bank,
 * the preview and the live panels.
 *
 * @author Michael
 */
public class MainPanel extends BorderPane {

    private static final Logger LOGGER = LoggerUtils.getLogger();
    private final SchedulePanel schedulePanel;
    private final LibraryPanel libraryPanel;
    private final PreviewPanel previewPanel;
    private final LivePanel livePanel;
    private final StatusPanelGroup statusPanelGroup;
    private final SplitPane mainSplit;
    private final SplitPane scheduleAndLibrary;

    /**
     * Create the new main panel.
     */
    public MainPanel() {
        LOGGER.log(Level.INFO, "Creating schedule panel");
        schedulePanel = new SchedulePanel();
        LOGGER.log(Level.INFO, "Creating library panel");
        libraryPanel = new LibraryPanel();
        LOGGER.log(Level.INFO, "Creating preview panel");
        previewPanel = new PreviewPanel();
        LOGGER.log(Level.INFO, "Creating live panel");
        livePanel = new LivePanel();

        LOGGER.log(Level.INFO, "Creating split panels");
        scheduleAndLibrary = new SplitPane();
        scheduleAndLibrary.setMinWidth(160);
        scheduleAndLibrary.setOrientation(Orientation.VERTICAL);
        scheduleAndLibrary.getItems().add(schedulePanel);
        scheduleAndLibrary.getItems().add(libraryPanel);

        // Link the preview and live dividers on the main window if the user wants them to move together
        if(  QueleaProperties.get().getLinkPreviewAndLiveDividers() ) {
            previewPanel.getLyricsPanel().getSplitPane().getDividers().get(0).positionProperty().
                    bindBidirectional(livePanel.getLyricsPanel().getSplitPane().getDividers().get(0).positionProperty());
        }

        previewPanel.getLyricsPanel().getSplitPane().setDividerPositions(0.58);
        livePanel.getLyricsPanel().getSplitPane().setDividerPositions(0.58);

        mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);
        mainSplit.getItems().add(scheduleAndLibrary);
        mainSplit.getItems().add(previewPanel);
        mainSplit.getItems().add(livePanel);
        setCenter(mainSplit);
        statusPanelGroup = new StatusPanelGroup();
        setBottom(statusPanelGroup);
        LOGGER.log(Level.INFO, "Created main panel");
    }

    /**
     * Set the position of the dividers based on the properties file.
     */
    public void setSliderPos() {
        double mainPos = QueleaProperties.get().getMainDivPos();
        double prevLivePos = QueleaProperties.get().getPrevLiveDivPos();
        double canvasPos = QueleaProperties.get().getCanvasDivPos();
        double previewPos = QueleaProperties.get().getPreviewDivposKey();
        double libraryPos = QueleaProperties.get().getLibraryDivPos();
        if (prevLivePos != -1 && mainPos != -1) {
            mainSplit.setDividerPositions(mainPos, prevLivePos);
        } else {
            mainSplit.setDividerPositions(0.2717, 0.6384);
        }

        if (canvasPos != -1 && (QueleaProperties.get().getLinkPreviewAndLiveDividers() || previewPos == -1 ) ) {
            // live divider pos is found and either the preview and live dividers are linked, or we don't have a
            // position saved for the preview divider so set both live and preview to saved value for live
            previewPanel.getLyricsPanel().getSplitPane().setDividerPositions(canvasPos);
            livePanel.getLyricsPanel().getSplitPane().setDividerPositions(canvasPos);
        } else if (canvasPos != -1) {
            // live and preview dividers not linked, and we have a position saved for the live divider so just set live
            livePanel.getLyricsPanel().getSplitPane().setDividerPositions(canvasPos);
        }

        if (previewPos != -1 && !QueleaProperties.get().getLinkPreviewAndLiveDividers()) {
            // preview divider pos is found, and not linked with live divider, so just set preview divider position
            previewPanel.getLyricsPanel().getSplitPane().setDividerPositions(previewPos);
        }

        if (libraryPos != -1) {
            scheduleAndLibrary.setDividerPositions(libraryPos);
        } else {
            scheduleAndLibrary.setDividerPositions(0.5);
        }
    }

    /**
     * Get the main splitpane divider position.
     *
     * @return the main splitpane divider position.
     */
    public double getMainDivPos() {
        return mainSplit.getDividerPositions()[0];
    }

    /**
     * Get the preview / live splitpane divider position.
     *
     * @return the preview / live splitpane divider position.
     */
    public double getPrevLiveDivPos() {
        return mainSplit.getDividerPositions()[1];
    }

    /**
     * Get the library / schedule splitpane divider position.
     *
     * @return the library / schedule splitpane divider position.
     */
    public double getLibraryDivPos() {
        return scheduleAndLibrary.getDividerPositions()[0];
    }

    /**
     * Get the panel displaying the selection of the preview lyrics.
     *
     * @return the panel displaying the selection of the preview lyrics.
     */
    public PreviewPanel getPreviewPanel() {
        return previewPanel;
    }

    /**
     * Get the panel displaying the selection of the live lyrics.
     *
     * @return the panel displaying the selection of the live lyrics.
     */
    public LivePanel getLivePanel() {
        return livePanel;
    }

    /**
     * Get the panel displaying the order of service.
     *
     * @return the panel displaying the order of service.
     */
    public SchedulePanel getSchedulePanel() {
        return schedulePanel;
    }

    /**
     * Get the panel displaying the library of media.
     *
     * @return the library panel.
     */
    public LibraryPanel getLibraryPanel() {
        return libraryPanel;
    }

    /**
     * Get the status panel.
     *
     * @return the status panel.
     */
    public StatusPanelGroup getStatusPanelGroup() {
        return statusPanelGroup;
    }
}
