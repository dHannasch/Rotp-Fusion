/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.game;

import static rotp.model.game.IGameOptions.LIVE_OPTIONS_FILE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.IParam;
import rotp.util.ModifierKeysState;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
abstract class AbstractOptionsUI extends BaseModPanel implements MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static final Color backgroundHaze = new Color(0,0,0,160);
	private final String guiTitleID;
	private final String GUI_ID;
	
	private Font descFont = narrowFont(15);
	private static int columnPad	= s20;
	private static int smallButtonH = s30;
	private static int hSetting	    = s90;
	private static int lineH		= s17;
	private static int rowPad		= s20;
	private static int hDistSetting = hSetting + rowPad; // distance between two setting top corner
	private int leftM, rightM, topM, yTop;
	private int wBG, hBG;
	private int numColumns, numRows;
	private int yTitle, xDesc, yDesc, yButton;
	private int xSetting, ySetting, wSetting; // settings var
	private int index, column;
	
	private Color textC = SystemPanel.whiteText;
	private LinkedList<Integer>	lastRowList = new LinkedList<>();
	private LinkedList<ModText> btList		= new LinkedList<>();
	private Box exitBox	= new Box(exitKey);
	private LinearGradientPaint bg;

	
	// ========== Constructors and initializers ==========
	//
	AbstractOptionsUI(String guiTitle_ID, String guiId) {
		guiTitleID = guiTitle_ID;
		GUI_ID = guiId;
	}
	@Override protected void singleInit() {
		setOpaque(false);
		textC = SystemPanel.whiteText;
		numColumns = 0;
		// Call for filling the settings
		init0();
		
		if (paramList == null) 
			activeList = duplicateList;
		else
			activeList = paramList;
		
		buildRowCountList();
		
		for (int i=0; i<activeList.size(); i++)
			btList.add(newBT().initGuide(activeList.get(i)));

		// numRows = Max column length
		numRows	 = lastRowList.getFirst();
		for (int i=1; i<lastRowList.size(); i++) {
			numRows = max(numRows, lastRowList.get(i)-lastRowList.get(i-1));
		}
		// Elements positioning
		int shiftTitle	= s40;
		int shiftButton	= s15;
		int topPad		= hSetting;
		int hSettings	= hDistSetting * numRows;

		// ----- With Management
		if (numColumns == 4)
			columnPad = s12;

		leftM	= max(columnPad, scaled(100 + (3-numColumns) * 150));
		rightM	= leftM;
		xDesc	= leftM + columnPad/2;

		// ----- Height Management
		if (numRows == 6) {
			topPad		-= s40; // Push the settings up			
			shiftTitle	-= s10; // Shift the title a little
		}	
		hBG		= topPad + hSettings + smallButtonH + s10;
		topM	= (h - hBG)/2;
		yTop	= topM + topPad; // First setting top position
		yButton	= yTop + hSettings - shiftButton + s10;
		yTitle	= topM + shiftTitle;
		yDesc	= yTitle + s20;
		
		// Special positioning for 6 rows
		if (numRows == 6) {
			// Move the description to the Title level
			yDesc	= yTitle;
		}

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	private void buildRowCountList() {
		numColumns = 1;
		Integer numParam = 0;
		for (IParam param : activeList) {
			if (param == null) {
				numColumns++;
				lastRowList.add(numParam);
			}
			else
				numParam++;
		}
		lastRowList.add(numParam);
		while (activeList.remove(null));
	}
	// ========== Abstract Methods Request ==========
	//
	protected abstract void init0();
	// ========== Other Methods ==========
	//
	private  ModText newBT() { 
		return new ModText(this, 20,  textC, textC, hoverC, depressedC, textC, true);
	}
	private void drawButtons(Graphics2D g) {
		int cnr = s5;
		// draw settings button
		int smallButtonW = scaled(180);
		exitBox.setBounds(w-scaled(189)-rightM, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(exitBox.x, exitBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		String text = text(exitButtonKey());
		int sw = g.getFontMetrics().stringWidth(text);
		int x = exitBox.x+((exitBox.width-sw)/2);
		int y = exitBox.y+exitBox.height-s8;
		Color c = hoverBox == exitBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		Stroke prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(exitBox.x, exitBox.y, exitBox.width, exitBox.height, cnr, cnr);
		g.setStroke(prev);

		text = text(defaultButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = defaultButtonWidth(g);
		defaultBox.setBounds(exitBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(defaultBox.x, defaultBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		x = defaultBox.x+((defaultBox.width-sw)/2);
		y = defaultBox.y+defaultBox.height-s8;
		c = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
		g.setStroke(prev);

		if (globalOptions)
			return;  // No User preferred button an no Last button
		text = text(lastButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = defaultButtonWidth(g);
		lastBox.setBounds(defaultBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(lastBox.x, lastBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		x = lastBox.x+((lastBox.width-sw)/2);
		y = lastBox.y+lastBox.height-s8;
		c = hoverBox == lastBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(lastBox.x, lastBox.y, lastBox.width, lastBox.height, cnr, cnr);
		g.setStroke(prev);

		text = text(userButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = userButtonWidth(g);
		userBox.setBounds(lastBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(userBox.x, userBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		x = userBox.x+((userBox.width-sw)/2);
		y = userBox.y+userBox.height-s8;
		c = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);

		text = text(guideButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = guideButtonWidth(g);
		guideBox.setBounds(leftM+s9, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(guideBox.x, guideBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		x = guideBox.x+((guideBox.width-sw)/2);
		y = guideBox.y+guideBox.height-s8;
		c = hoverBox == guideBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(guideBox.x, guideBox.y, guideBox.width, guideBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	private void paintSetting(Graphics2D g, ModText txt, String desc) {
		g.setColor(SystemPanel.blackText);
		g.drawRect(xSetting, ySetting, wSetting, hSetting);
		g.setPaint(bg);
		g.fillRect(xSetting+s10, ySetting-s10, txt.stringWidth(g)+s10,s30);
		txt.setScaledXY(xSetting+columnPad, ySetting+s7);
		txt.draw(g);
		g.setColor(SystemPanel.blackText);
		g.setFont(descFont);
		List<String> lines = wrappedLines(g, desc, wSetting-s30);
		int y3 = ySetting+s10;
		for (String line: lines) {
			y3 += lineH;
			drawString(g,line, xSetting+columnPad, y3);
		}		
	}
	private void goToNextSetting() {
		index++;
		if (index >= lastRowList.get(column)) {
			column++;
			if (column == numColumns) {
				// System.err.println(GUI_ID + ": column > numColumns");
				column--;
			}
			xSetting = xSetting + wSetting + columnPad;
			ySetting = yTop;
		} else
			ySetting += hDistSetting;
	}
	private void mouseCommon(boolean up, boolean mid, boolean shiftPressed, boolean ctrlPressed
			, MouseEvent e, MouseWheelEvent w) {
		for (int i=0; i<activeList.size(); i++) {
			if (hoverBox == btList.get(i).box()) {
				if (activeList.get(i).isSubMenu()) {
					if (e == null)
						return;
					super.close();
			        disableGlassPane();
					activeList.get(i).toggle(e, 2, this);
					return;
				}			
				activeList.get(i).toggle(e, w, this);
				btList.get(i).repaint(activeList.get(i).getGuiDisplay());
				if (autoGuide) {
					loadGuide();
					repaint();
				}
				return;
			}			
		}
	}
	private void setLocalToDefault() {
		for (InterfaceOptions param : activeList)
			param.setFromDefault();
	}
	// ========== Overriders ==========
	//
	@Override public void init()	 {
		super.init();
		w	= RotPUI.setupRaceUI().getWidth();
		h	= RotPUI.setupRaceUI().getHeight();
		wBG	= w - (leftM + rightM);
		wSetting = (wBG/numColumns)-columnPad;
		if (bg == null)
			if (numColumns>3)
				bg = GameUI.settingsSetupBackgroundW(w);
			else
				bg = GameUI.settingsSetupBackground(w);
		if (!globalOptions) // The new ways
			guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
		enableGlassPane(this);
		refreshGui();
	}
	@Override protected void close() {
		super.close();
        disableGlassPane();
		RotPUI.setupGalaxyUI().init();
	}
	@Override protected void doExitBoxAction()		{
		if (globalOptions) { // The old ways
			buttonClick();
			UserPreferences.save();
			close();			
		}
		else
			super.doExitBoxAction();
	}
	@Override protected void doDefaultBoxAction()	{
		if (globalOptions) { // The old ways
			buttonClick();
			switch (ModifierKeysState.get()) {
			case CTRL:
			case CTRL_SHIFT: // cancelKey
				UserPreferences.load();
				break;
			default: // setLocalDefaultKey
				setLocalToDefault();
				break; 
			}
			refreshGui();
		}
		else
			super.doDefaultBoxAction();
	}
	@Override protected void refreshGui()	{
		super.refreshGui();
		for (int i=0; i<activeList.size(); i++)
			btList.get(i).displayText(activeList.get(i).getGuiDisplay());
		repaint();
	}
	@Override public void repaintButtons()	{
		Graphics2D g = (Graphics2D) getGraphics();
		setFontHints(g);
		drawButtons(g);
		g.dispose();
	}
	@Override protected String GUI_ID()		{ return GUI_ID; }
	@Override public void paintComponent(Graphics g0)	{
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		g.setPaint(bg);
		g.fillRect(leftM, topM, wBG, hBG);
		g.setFont(narrowFont(30));
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = (w-sw)/2;
		if (numRows == 6) {
			xTitle = w -rightM - sw - 2*columnPad;
		}

		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
		g.setFont(narrowFont(18));
		String expl = text("SETTINGS_DESCRIPTION");
		g.setColor(SystemPanel.blackText);
		drawString(g, expl, xDesc, yDesc);
		
		Stroke prev = g.getStroke();
		g.setStroke(stroke3);
		
		// Loop thru the parameters
		index	 = 0;
		column	 = 0;
		xSetting = leftM + columnPad/2;
		ySetting = yTop;
		// First column (left)
		while (index<activeList.size()) {
			paintSetting(g, btList.get(index), activeList.get(index).getGuiDescription());
			goToNextSetting();
		}
		g.setStroke(prev);
		drawButtons(g);
		showGuide(g);
	}
	@Override public void keyPressed(KeyEvent e)		{
		super.keyPressed(e);
		switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				doExitBoxAction();
				return;
			case KeyEvent.VK_SPACE:
			default: // BR:
		}
	}
	@Override public void mouseMoved(MouseEvent e)		{
		mX = e.getX();
		mY = e.getY();
		if (hoverBox != null && hoverBox.contains(mX,mY)) {
			hoverChanged = false;
			return;
		}
		prevHover	 = hoverBox;
		hoverBox	 = null;
		hoverChanged = true;
		for (Box box : boxBaseList)
			if (box.checkIfHovered())
				break;
		if (prevHover != null) {
			prevHover.mouseExit();
			loadGuide();
			repaint();
		}
	}
	@Override public void mouseReleased(MouseEvent e)	{
		checkModifierKey(e);
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		boolean up	= !SwingUtilities.isRightMouseButton(e); // BR: added bidirectional
		boolean mid	= !SwingUtilities.isMiddleMouseButton(e); // BR: added reset click
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed  = e.isControlDown();
		mouseCommon(up, mid, shiftPressed, ctrlPressed, e, null);
		if (hoverBox == exitBox)
			doExitBoxAction();
		else if (hoverBox == defaultBox)
			doDefaultBoxAction();
		else if (hoverBox == userBox)
			doUserBoxAction();
		else if (hoverBox == guideBox)
			doGuideBoxAction();
		else if (hoverBox == lastBox)
			doLastBoxAction();
	}
	@Override public void mouseEntered(MouseEvent e)	{
		for (int i=0; i<activeList.size(); i++) {
			if (hoverBox == btList.get(i).box()) {	
				btList.get(i).repaint(activeList.get(i).getGuiDisplay());
				if (autoGuide) {
					loadGuide();
					repaint();
				}
				return;
			}			
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		checkModifierKey(e);
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed  = e.isControlDown();
		boolean up = e.getWheelRotation() < 0;
		mouseCommon(up, false, shiftPressed, ctrlPressed, null, e);
	}
}
