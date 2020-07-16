/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chihuasdevs.utils;

import java.util.prefs.Preferences;

/**
 *
 * @author aogutier
 */
public class UserPreferencesUtil {
    
    public static class Paths{
        private static final Preferences PREFS = Preferences.userRoot().node("Paths");
       
        public enum PathsKeys{
            LastSavedProjectPath,
            LastOpenProjectPath
        }

        public static void setLastSavedProjectPath(String lastSavedProjectPath){
            PREFS.put(PathsKeys.LastSavedProjectPath.name(), lastSavedProjectPath);
        }
        public static String getLastSavedProjectPath(){
            
            if (PREFS.get(PathsKeys.LastSavedProjectPath.name(), null) == null){
                return null;
            }
            return PREFS.get(PathsKeys.LastSavedProjectPath.name(), null);
        }
        
        public static void setLastOpenProjectPath(String lastSavedProjectPath){
            PREFS.put(PathsKeys.LastOpenProjectPath.name(), lastSavedProjectPath);
        }
        
        public static String getLastOpenProjectPath(){
            
            if (PREFS.get(PathsKeys.LastOpenProjectPath.name(), null) == null){
                return null;
            }
            return PREFS.get(PathsKeys.LastOpenProjectPath.name(), null);
        }
    }
    
}
