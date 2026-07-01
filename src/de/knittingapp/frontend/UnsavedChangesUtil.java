package de.knittingapp.frontend;

import javafx.scene.control.Tab;

public class UnsavedChangesUtil {
    private static Tab mainTab;

    public static void init(Tab mainTab){
        UnsavedChangesUtil.mainTab = mainTab;
    }


    public static void markUnsavedChanges(){
        if(!checkForUnsavedChanges()) {
            mainTab.setText(mainTab.getText() + " *");
        }
    }

    public static boolean checkForUnsavedChanges(){
        String tabTitle = mainTab.getText();
        return tabTitle.endsWith("*");
    }

    public static void removeMarker(){
        if (checkForUnsavedChanges()){
            String tabTitle = mainTab.getText();
            mainTab.setText(tabTitle.substring(0,tabTitle.length()-1));
        }
    }
}
