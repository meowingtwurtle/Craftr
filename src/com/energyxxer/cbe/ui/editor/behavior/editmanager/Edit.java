package com.energyxxer.cbe.ui.editor.behavior.editmanager;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;

/**
 * Created by User on 1/10/2017.
 */
public interface Edit {
    void redo(AdvancedEditor editor);
    void undo(AdvancedEditor editor);
}
