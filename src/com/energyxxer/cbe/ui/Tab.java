package com.energyxxer.cbe.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.logic.Project;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.editor.CBEEditor;

/**
 * Concept of an open tab in the interface. Contains a component that represents
 * the clickable tab element.
 */
public class Tab {
	private TabComponent linkedTabComponent;
	private Project linkedProject;
	public String path;
	public CBEEditor editor;
	public String savedString;
	public boolean visible = true;

	public long openedTimeStamp;

	@Override
	public String toString() {
		return "Tab [title=" + linkedTabComponent.getName() + ", path=" + path + ", visible=" + visible + "]";
	}

	public Tab(String path) {
		this.path = path;
		this.linkedProject = ProjectManager.getAssociatedProject(new File(path));
		editor = new CBEEditor(this);
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			String s = new String(encoded);
			editor.setText(s);
			editor.startEditListeners();
			savedString = s.intern();
		} catch (IOException e) {
			e.printStackTrace();
		}

		associate(new TabComponent(this));

		TabManager.addTabComponent(getLinkedTabComponent());

		openedTimeStamp = new Date().getTime();
	}

	public void associate(TabComponent tc) {
		linkedTabComponent = tc;
	}

	private File getFile() {
		return new File(path);
	}

	public TabComponent getLinkedTabComponent() {
		return linkedTabComponent;
	}

	public void onSelect() {
		openedTimeStamp = new Date().getTime();
	}

	public void onEdit() {
		if (linkedTabComponent != null) {
			boolean newIsSaved = editor.editor.getText().intern() == savedString || savedString == null;
			linkedTabComponent.setSaved(newIsSaved);
		}
	}

	public void updateName() {
		linkedTabComponent.setName(getFile().getName());
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void save() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");

			writer.print(editor.editor.getText());
			writer.close();
			savedString = editor.editor.getText().intern();
			linkedTabComponent.setSaved(true);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace(new PrintWriter(Window.consoleOut));
		}
	}

	public Project getLinkedProject() {
		return linkedProject;
	}
}