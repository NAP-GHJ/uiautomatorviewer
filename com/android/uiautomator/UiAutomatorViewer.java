/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.uiautomator;

import com.android.uiautomator.actions.OpenFilesAction;
import com.android.uiautomator.actions.SaveScreenShotAction;
import com.android.uiautomator.actions.ScreenshotAction;
import com.android.uiautomator.actions.GenerateXpathFileAction;
import com.android.uiautomator.actions.ControlDefineAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import java.io.File;

/**
 * @author huangshuli
 * @Description 程序入口
 */
public class UiAutomatorViewer extends ApplicationWindow {
    {
        System.out.println("类被加载了");
    }

    private UiAutomatorView mUiAutomatorView;
    private UiAutomatorModel uModel;
    private ControlDefineAction controlDefineAction;
    public UiAutomatorViewer() {
        super(null);
        System.out.println("类被加载了");

    }

    @Override
    protected Control createContents(Composite parent) {
        Composite c = new Composite(parent, SWT.BORDER);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        c.setLayout(gridLayout);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        c.setLayoutData(gd);
        
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        toolBarManager.add(new OpenFilesAction(this));
        toolBarManager.add(new ScreenshotAction(this,false));
        toolBarManager.add(new ScreenshotAction(this,true));
        toolBarManager.add(new SaveScreenShotAction(this));
        toolBarManager.add(new GenerateXpathFileAction(this));
        controlDefineAction = new ControlDefineAction(this);
        toolBarManager.add(controlDefineAction);
        ToolBar tb = toolBarManager.createControl(c);
        tb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        mUiAutomatorView = new UiAutomatorView(c, SWT.BORDER, this);
        mUiAutomatorView.setLayoutData(new GridData(GridData.FILL_BOTH));

        return parent;
    }

    public static void main(String args[]) {
        System.out.println("程序被正常启动了");
        DebugBridge.init();
        try {
            UiAutomatorViewer window = new UiAutomatorViewer();
            window.setBlockOnOpen(true);
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Lazy Ui Automator Viewer");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(1200, 900);
    }

    public void setModel(final UiAutomatorModel model, final File modelFile,final Image screenshot) {
        if (Display.getDefault().getThread() != Thread.currentThread()) {
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    mUiAutomatorView.setModel(model, modelFile, screenshot);
                }
            });
        } else {
            mUiAutomatorView.setModel(model, modelFile, screenshot);
        }
        uModel = model;
    }
    public Image getScreenShot() {
        return mUiAutomatorView.getScreenShot();
    }
    public File getModelFile(){
        return mUiAutomatorView.getModelFile();
    }
    public UiAutomatorModel getModel(){
        return uModel;
    }

    public void updateInfo() {
        controlDefineAction.updateDlg();
    }

}