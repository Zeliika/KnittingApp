package de.knittingapp.frontend;

import de.knittingapp.dto.KnittingInstruction;
import de.knittingapp.dto.KnittingPattern;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * util class for managing tabs in TabPane in KnittingApp
 */
public class TabPaneUtil {
    private static TabPane tabPane;

    public static void initTabPane(TabPane tabPane){
        TabPaneUtil.tabPane = tabPane;
    }

    /**
     * adds tab with layout for KnittingInstruction if tab doesn't exist
     * sets focus to tab
     * @param id id of the KnittingInstruction
     */
    public static void addKnittingInstructionsTab(int id){
        for (Tab tab : tabPane.getTabs()){
            if(tab.getUserData() == null){
                continue;
            }
            if ((int)tab.getUserData() == id){
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }
        KnittingInstruction knittingInstruction = KnittingApp.knittingProject.getKnittingInstructions().get(id);
        Tab newTab = new Tab(knittingInstruction.getName());
        newTab.setUserData(id);
        if (knittingInstruction instanceof KnittingPattern){
            KnittingPatternLayout layout = new KnittingPatternLayout((KnittingPattern) knittingInstruction);
            newTab.setContent(layout);
        } else {
            KnittingInstructionLayout layout = new KnittingInstructionLayout(knittingInstruction);
            newTab.setContent(layout);
        }
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }

    /**
     * removes tab from tabPane if userData of tab equals id
     * @param id of the KnittingInstruction connected with tab
     */
    public static void closeTab(int id){
        tabPane.getTabs().removeIf(tab -> (tab != tabPane.getTabs().getFirst() && (int) tab.getUserData() == id));
    }

    public static void updateTabTitles(){
        for (Tab tab : tabPane.getTabs()){
            if (tab.getContent() instanceof KnittingProjectLayout){
                tab.setText(KnittingApp.knittingProject.getTitle());
            } else {
                int id = (int)tab.getUserData();
                tab.setText(KnittingApp.knittingProject.getKnittingInstructions().get(id).getName());
            }
        }
    }
}
