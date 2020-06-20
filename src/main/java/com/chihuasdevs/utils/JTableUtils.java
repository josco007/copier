
package com.chihuasdevs.utils;


import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author noe_i
 */
public class JTableUtils {
    
    public interface UpDownUtilityEvents{
        void onOtherKeyEvent(KeyEvent e);
        void onEnter();
        void onRowMoved();
        
    }
    
    public static enum TypesOfSelectUpDownUtil{
        SelectFirstRow,
        SelectLastRow,
    }
    
    
    public static int getColumnByName(JTable table, String name) {
        for (int i = 0; i < table.getColumnCount(); ++i)
            if (table.getColumnName(i).equals(name))
                return i;
        return -1;
    }

    public static DefaultTableModel getEmptyDefaultTableModel(JTable table){

        int numeroDeFilas = table.getRowCount();
            if (numeroDeFilas != 0) // si hay alguna fila en la tabla las va a borrar
            {
                for (int i = 0; i < numeroDeFilas; i++) {// for que nos ayuda a borrar todas las filas
                    ((DefaultTableModel)table.getModel()).removeRow(0);// quita la fila 0
                }
            }
            
            return (DefaultTableModel) table.getModel();
    }

    public static void selectFirstRowIfPossible(JTable table, boolean onlyIfNotSelected){
        if (onlyIfNotSelected && table.getSelectedRow() != -1){
            return;
        }
        if (table.getRowCount() > 0){
            table.setRowSelectionInterval(0, 0);
        }
    }
    public static void selectLastRowIfPossible(JTable table, boolean onlyIfNotSelected){
        if (onlyIfNotSelected && table.getSelectedRow() != -1){
            return;
        }
        if (table.getRowCount() > 0){
            table.setRowSelectionInterval(table.getRowCount()-1 , table.getRowCount()-1);
        }
    }
    
    public static void moveSelectedRowIfPossible(JTable table, KeyEvent e){
        if (table.getRowCount() > 1){
            
            if (table.getSelectedRow() == -1){
                table.setRowSelectionInterval(0, 0);
            }
            
            int currentRow = table.getSelectedRow();
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP:
                    if (currentRow == 0 ){
                        return;
                    }
                    
                    table.setRowSelectionInterval(currentRow - 1, currentRow - 1);
                    break;
                case KeyEvent.VK_DOWN:{
                    if (currentRow == (table.getRowCount() - 1)){
                        return;
                    }
                    
                    table.setRowSelectionInterval(currentRow + 1, currentRow + 1);
                    table.changeSelection(currentRow+1, 0, false, false);
                    break;
                }
            }
        }
    }
    
    public static DefaultTableModel getNewDefaultTableModel(Object[] columnNames){
        return new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    
    
    /**
    * Mueve el seleccionador de filas de una tabla con las flechas del teclado.
    * 
     * @param table tabla a la que se quiere mover el seleccionador con las felchas del teclado
     * @param e KeyEvent registrado
     * @param upDownUtilityEvents listener de eventos que regresa la utileria.
     * <p>
     * onEnter detecta el evento de la tecla enter en el componente.
     * <p>
     * onOtherKeyEvent regresa el evento cuando la tecla pulsada no se encuentra dentro de VK_DOWN, VK_UP o VK_ENTER
     * 
     * 
    * @deprecated  use instead {@link #addUpDownToComponentForTable(javax.swing.JTable, java.awt.Component, utils.JTableUtils.TypesOfSelectUpDownUtil, utils.JTableUtils.UpDownUtilityEvents) }
    */
   @Deprecated public static void upDownUtility(JTable table, KeyEvent e, UpDownUtilityEvents upDownUtilityEvents){
        if(e.getKeyCode() != KeyEvent.VK_DOWN && 
                e.getKeyCode() != KeyEvent.VK_UP &&
                e.getKeyCode() != KeyEvent.VK_ENTER){
            upDownUtilityEvents.onOtherKeyEvent(e);
            JTableUtils.selectFirstRowIfPossible(table, true);
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            if (table.getSelectedRow() != -1){
                upDownUtilityEvents.onEnter();
                return;
            }
        }

        JTableUtils.moveSelectedRowIfPossible(table, e);
    }
    /**
     * Mueve el seleccionador de filas de una tabla hacia arriba o hacia abajo cuando se detecta un evento del teclado en las flechas arriba y abajo 
     * @param table tabla a la que se le desea agregar el funcionamiento
     * @param component componente en el que se desea capturar eventos  de flechas del teclado para mover el seleccionador de filas de la tabla
     * @param typesOfSelectUpDownUtil tipo de seleccionado cuando se detecta cualquier otro evento que no sea VK_DOWN, VK_UP o VK_ENTER
     * <p>
     * Se puede especificar por defecto que se seleccione la primera o la ultima fila
     * @param upDownUtilityEvents listener de eventos que regresa la utileria.
     * <p>
     * onEnter detecta el evento de la tecla enter en el componente.
     * <p>
     * onOtherKeyEvent regresa el evento cuando la tecla pulsada no se encuentra dentro de VK_DOWN, VK_UP o VK_ENTER
     */
    public static void addUpDownToComponentForTable(JTable table,
            Component component, 
            TypesOfSelectUpDownUtil typesOfSelectUpDownUtil,
            List<Character> skipSelectFirstOrLastChars,
            UpDownUtilityEvents upDownUtilityEvents){
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                
                if(e.getKeyCode() != KeyEvent.VK_DOWN && 
                        e.getKeyCode() != KeyEvent.VK_UP &&
                        e.getKeyCode() != KeyEvent.VK_ENTER){
                    
                    upDownUtilityEvents.onOtherKeyEvent(e);
                    
                    if (skipSelectFirstOrLastChars.contains(e.getKeyChar())){
                        return;
                    }
                    
                    switch(typesOfSelectUpDownUtil){
                        case SelectFirstRow:
                            JTableUtils.selectFirstRowIfPossible(table, true);    
                            break;
                        case SelectLastRow:
                            JTableUtils.selectLastRowIfPossible(table, true);
                            break;
                    }
                    
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    if (table.getSelectedRow() != -1){
                        upDownUtilityEvents.onEnter();
                        return;
                    }
                }
                JTableUtils.moveSelectedRowIfPossible(table, e);
                upDownUtilityEvents.onRowMoved();
            } 
        });
    }
    
    
    public static void showCell(JTable table,int row, int column) {
        if (table.getRowCount() == 0){
            return;
        }
        Rectangle rect = table.getCellRect(row, column, true);
        table.scrollRectToVisible(rect);
        table.clearSelection();
        table.setRowSelectionInterval(row, row);
        table.getModel();
    }
    
    public static void scrollToLast(JTable table){;
        showCell(table , table.getRowCount () - 1, 0);
    }
    
}
