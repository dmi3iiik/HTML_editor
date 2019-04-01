package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.UIManager.getSystemLookAndFeelClassName;

public class View extends JFrame implements ActionListener {
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();

    public View() {
        try {
            UIManager.setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    public void undo() {
        try {
            undoManager.undo();
        } catch (CannotUndoException e) {
            ExceptionHandler.log(e);
        }
    }
    public void redo() {
        try {
            undoManager.redo();
        } catch (CannotRedoException e) {
            ExceptionHandler.log(e);
        }
    }

    public boolean canRedo() {
        try {
            return undoManager.canRedo();
        } catch (CannotRedoException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean canUndo() {
        try {
            return undoManager.canUndo();
        } catch (CannotUndoException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public void init() {
        addWindowListener(new FrameListener(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initGui();
        setVisible(true);
    }
    public void exit() {
        controller.exit();
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        switch (command) {
            case "Новый": {
                controller.createNewDocument();
            } break;
            case "Открыть": {
                controller.openDocument();
            } break;
            case "Сохранить": {
                controller.saveDocument();
            } break;
            case "Сохранить как...": {
                controller.saveDocumentAs();
            } break;
            case "Выход": {
                exit();
            } break;
            case "О программе": {
                showAbout();
            } break;
        }

    }
    public void initMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);
        getContentPane().add(jMenuBar, BorderLayout.NORTH);

    }

    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        JScrollPane jScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML", jScrollPane);
        JScrollPane jScrollPane2 = new JScrollPane(plainTextPane);
        tabbedPane.add("Текст", jScrollPane2);
        tabbedPane.setPreferredSize(new Dimension());
        tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }
    public void selectedTabChanged() {
        if (tabbedPane.getSelectedIndex() == 0) {
            controller.setPlainText(plainTextPane.getText());
        } else if (tabbedPane.getSelectedIndex() == 1) {
            plainTextPane.setText(controller.getPlainText());
        }
        this.resetUndo();
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }
    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }
    public void showAbout() {
        String message = "Here is information about app";
        JOptionPane.showMessageDialog(null, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    public boolean isHtmlTabSelected() {
        if (tabbedPane.getSelectedIndex() == 0) {
            return true;
        }
        else {
            return false;
        }
    }


}
