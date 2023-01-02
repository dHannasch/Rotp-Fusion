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

import static rotp.model.game.MOO1GameOptions.updateOptionsAndSaveToFileName;
import static rotp.ui.UserPreferences.ALL_GUI_ID;
import static rotp.ui.UserPreferences.LIVE_OPTIONS_FILE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import rotp.mod.br.profiles.Profiles;
import rotp.ui.BaseText;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.Modifier2KeysState;
import rotp.util.FontManager;

class CompactOptionsUI extends BaseModPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static final Color backgroundHaze = new Color(0,0,0,160);
	private final String guiTitleID;
	private final String GUI_ID;
	
	private static final int rowPad			= s10;
	private	static final int descPadV		= 0;
	private	static final int descPadM		= s10;
	private static final int buttonPadV		= rowPad;
	private static final Color descColor	= SystemPanel.blackText;
	private static final Color disabledColor= GameUI.textColor();
	private static final Color enabledColor	= GameUI.labelColor();
//	private static final Color enabledColor	= GameUI.textColor();
//	private static final Color disabledColor= GameUI.labelColor();
//	private static final Color disabledColor= GameUI.disabledTextColor();
//	private static final Color enabledColor	= SystemPanel.whiteText;
//	private static final Color disabledColor= SystemPanel.blackText;
	private static final int descLineH		= 18;
	private	static final Font descFont		= FontManager.current().narrowFont(16);
	private	static final Font titleFont		= FontManager.current().narrowFont(30);
	private static final int titleOffset	= s40; // Offset from Margin
	private static final int titlePad		= s70; // Offset of first setting
	private static final int settingFont	= 20;
	private static final int settingH		= s20;
	private static final int settingpadH	= s6;
	private static final int columnPad		= s12;
	private static final int tooltipLines	= 2;
	private static final int descHeigh		= tooltipLines * descLineH + descPadM;
	private static final int bottomPad		= rowPad;
	private static final int textPad		= rowPad;
	private static final int textBoxH		= settingH;
	private static final int hDistSetting	= settingH + settingpadH; // distance between two setting top corner
	private static final int leftM			= columnPad;
	private static final int rightM			= leftM;
	private int topM, yTop;
	private int wBG, hBG;
	private int numColumns, numRows;
	private int yTitle, yButton;
	private int xSetting, ySetting, columnWidth; // settings var
	private int index, column;
	private int xDesc, yDesc, descWidth;
	private int x, y; // mouse position
	
	private LinkedList<Integer>	lastRowList = new LinkedList<>();
	private LinkedList<BaseText> btList		= new LinkedList<>();
	private Rectangle hoverBox, prevHover;
	private final Rectangle exitBox		= new Rectangle();
	private final Rectangle toolTipBox	= new Rectangle();
	private LinearGradientPaint bg;

	private String tooltipText = "";
	private String preTipTxt	 = "";

	
	// ========== Constructors and initializers ==========
	//
	CompactOptionsUI(String guiTitle_ID, String guiId, LinkedList<LinkedList<InterfaceParam>> paramList) {
		guiTitleID = guiTitle_ID;
		GUI_ID = guiId;
		init_Lists(paramList);
		init_0();
	}
	private void init_Lists(LinkedList<LinkedList<InterfaceParam>> optionsList) {
		activeList		= new LinkedList<>();
		duplicateList	= new LinkedList<>();
		paramList		= new LinkedList<>();
		int totalRows   = 0;
		numColumns = optionsList.size();
		numRows    = 0;
		for (LinkedList<InterfaceParam> list : optionsList) {
			totalRows += list.size();
			lastRowList.add(totalRows);
			numRows = max(numRows, list.size());
			for (InterfaceParam param : list) {
				if (param != null) {
					activeList.add(param);
					btList.add(newBT(param.isTitle()));
					if (param.isDuplicate())
						duplicateList.add(param);
					else
						paramList.add(param);
				}
			}
		}
	}
	private void init_0() {
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	// ========== Other Methods ==========
	//
	private  BaseText newBT(boolean disabled) {
		BaseText bt = new BaseText(this, false, settingFont, 0, 0,
				enabledColor, disabledColor, hoverC, depressedC, enabledColor, 0, 0, 0);
		bt.disabled(disabled);
		return bt;
	}
	private void drawButtons(Graphics2D g) {
		int cnr = s5;
		// draw settings button
		int smallButtonW = scaled(180);
		exitBox.setBounds(w-scaled(189)-rightM, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(exitBox.x, exitBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(smallButtonFont);
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
		g.setFont(smallButtonFont);
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
		g.setFont(smallButtonFont);
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
		g.setFont(smallButtonFont);
		x = userBox.x+((userBox.width-sw)/2);
		y = userBox.y+userBox.height-s8;
		c = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	private void paintDescriptions(Graphics2D g) {
		List<String> lines = wrappedLines(g, tooltipText, descWidth-2*descPadM);
		g.setFont(descFont);
		toolTipBox.setBounds(xDesc, yDesc, descWidth, descHeigh);
		g.setColor(GameUI.setupFrame());
		g.fill(toolTipBox);
		g.setColor(descColor);
		int xT = xDesc+descPadM;
		int yT = yDesc + s4;
		for (String line: lines) {
			yT += descLineH;
			drawString(g,line, xT, yT);
		}		
	}
	private void repaintTooltip() {
		Graphics2D g = (Graphics2D) getGraphics();
		super.paintComponent(g);
		paintDescriptions(g);
		g.dispose();
	}
	private void paintSetting(Graphics2D g, BaseText txt) {
		g.setPaint(bg);
		g.fillRect(xSetting+textPad, ySetting-textPad, txt.stringWidth(g)+textPad, textBoxH);
		txt.setScaledXY(xSetting+columnPad, ySetting+s7);
		txt.draw(g);
	}
	private void goToNextSetting() {
		index++;
		if (index >= lastRowList.get(column)) {
			column++;
			xSetting = xSetting + columnWidth + columnPad;
			ySetting = yTop;
		} else
			ySetting += hDistSetting;
	}
	private void mouseCommon(MouseEvent e, MouseWheelEvent w) {
		for (int i=0; i<activeList.size(); i++) {
			if (hoverBox == btList.get(i).bounds()) {
				activeList.get(i).toggle(e, w);
				btList.get(i).repaint(activeList.get(i).getGuiDisplay());
				return;
			}			
		}
	}
	private void setLocalToDefault() {
		for (InterfaceOptions param : activeList)
			param.setFromDefault();
	}
	private boolean checkForHoveredButtons() {
		if (exitBox.contains(x,y)) {
			hoverBox = exitBox;
			tooltipText = text(exitButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (userBox.contains(x,y)) {
			hoverBox = userBox;
			tooltipText = text(userButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (lastBox.contains(x,y)) {
			hoverBox = lastBox;
			tooltipText = text(lastButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (defaultBox.contains(x,y)) {
			hoverBox = defaultBox;
			tooltipText = text(defaultButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		}
		return false;
	}
	private boolean checkForHoveredSettings(LinkedList<BaseText> baseTextList) {
		BaseText bt;
		for (int idx=0; idx<baseTextList.size(); idx++) {
			bt = baseTextList.get(idx);
			if (bt.contains(x,y)) {
				hoverBox = bt.bounds();
				tooltipText = activeList.get(idx).getGuiDescription();
				if (hoverBox != prevHover) {
					bt.mouseEnter();
					if (tooltipText.equals(preTipTxt)) { 
						repaint(hoverBox);
					} else {
						repaint(hoverBox);
						repaintTooltip();
					}
				}
				return true;
			}
		}
		return false;
	}
	private void checkExitSettings(LinkedList<BaseText> baseTextList) {
		for (BaseText setting : baseTextList) {
			if (prevHover == setting.bounds()) {
				setting.mouseExit();
				return;
			}
		}
	}
	private void hoverAndTooltip(boolean keyModifierChanged) {
		if (btList == null) {
			System.out.println("CompactOptionsUI: btList is null");
			return;
		}
		preTipTxt = tooltipText;
		tooltipText = "";
		prevHover = hoverBox;
		hoverBox = null;
		// Check if cursor is in a box
		boolean onButton = checkForHoveredButtons();
		boolean onBox = onButton || checkForHoveredSettings(btList);

		if (prevHover != hoverBox && prevHover != null) {
			checkExitSettings(btList);
			repaint(prevHover);
		}
		if (keyModifierChanged) {
			repaintButtons();
			if (!tooltipText.equals(preTipTxt))
				repaintTooltip();
		}
		else if (!onBox && prevHover != null) {
			if (!tooltipText.equals(preTipTxt))
				repaintTooltip();
		}
	}
	// ========== Overriders ==========
	//
	@Override public void init() {
		super.init();
		int hSettingTotal = hDistSetting * numRows;
		hBG	= titlePad + hSettingTotal + descPadV + descHeigh + buttonPadV + smallButtonH + bottomPad;
		hBG	= titlePad + hSettingTotal + descHeigh + buttonPadV + smallButtonH + bottomPad;
		wBG	= w - (leftM + rightM);
		topM	= (h - hBG) / 2;
		yTitle	= topM + titleOffset;
		yTop	= topM + titlePad; // First setting top position
		yButton	= topM + hBG - (smallButtonH + bottomPad);
		descWidth	= wBG - 2 * columnPad;
		xDesc		= leftM + columnPad;		
		yDesc		= topM + hBG - ( descHeigh + buttonPadV + smallButtonH + bottomPad);
		
		columnWidth = ((wBG-columnPad)/numColumns)-columnPad;
		if (bg == null)
			bg = GameUI.settingsSetupBackgroundW(w);
		updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		enableGlassPane(this);
		refreshGui();
	}
	@Override protected void checkModifierKey(InputEvent e) {
		hoverAndTooltip(Modifier2KeysState.checkForChange(e));
	}
	@Override protected void close() {
		super.close();
        disableGlassPane();
        if (!inGame())
        	RotPUI.setupGalaxyUI().init();
        else
        	RotPUI.instance().mainUI().map().resetRangeAreas();
	}
	@Override protected void doExitBoxAction() {
		if (globalOptions) { // The old ways
			buttonClick();
			UserPreferences.save();
			close();			
		}
		else
			super.doExitBoxAction();
	}
	@Override protected void doDefaultBoxAction() {
		if (globalOptions) { // The old ways
			buttonClick();
			switch (Modifier2KeysState.get()) {
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
	@Override protected void refreshGui() {
		super.refreshGui();
		for (int i=0; i<activeList.size(); i++)
			btList.get(i).displayText(activeList.get(i).getGuiDisplay());
		repaint();
	}
	@Override protected void repaintButtons() {
		Graphics2D g = (Graphics2D) getGraphics();
		setFontHints(g);
		drawButtons(g);
		g.dispose();
	}
	@Override protected String GUI_ID() { return GUI_ID; }
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		g.setPaint(bg);
		g.fillRect(leftM, topM, wBG, hBG);
		
		// Tool tip
		paintDescriptions(g);

		// Title
		g.setFont(titleFont);
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = (w-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
//		g.setFont(narrowFont(18));
//		String expl = text("SETTINGS_DESCRIPTION");
//		g.setColor(SystemPanel.blackText);
//		drawString(g, expl, xDesc, yDesc);
		
		Stroke prev = g.getStroke();
		g.setStroke(stroke3);
		
		// Loop thru the parameters
		index	 = 0;
		column	 = 0;
		xSetting = leftM + columnPad/2;
		ySetting = yTop;
		// First column (left)
		while (index<activeList.size()) {
			paintSetting(g, btList.get(index));
			goToNextSetting();
		}
		g.setStroke(prev);

		drawButtons(g);
		
	}
	@Override public void keyReleased(KeyEvent e) {
		checkModifierKey(e);		
	}
	@Override public void keyPressed(KeyEvent e) {
		checkModifierKey(e);
		int k = e.getKeyCode();  // BR:
		switch(k) {
			case KeyEvent.VK_ESCAPE:
				doExitBoxAction();
				return;
			case KeyEvent.VK_SPACE:
			default: // BR:
				if(Profiles.processKey(k, e.isShiftDown(), guiTitleID, newGameOptions())) {
					for (int i=0; i<activeList.size(); i++) {
						btList.get(i).repaint(activeList.get(i).getGuiDisplay());
					}
				};
				return;
		}
	}
	@Override public void mouseDragged(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		checkModifierKey(e);
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (exitBox.contains(x,y))
			hoverBox = exitBox;
		else if (defaultBox.contains(x,y))
			hoverBox = defaultBox;
		else if (userBox.contains(x,y))
			hoverBox = userBox;
		else if (lastBox.contains(x,y))
			hoverBox = lastBox;
		else for (BaseText txt : btList) {
			if (txt.contains(x,y)) {
				hoverBox = txt.bounds();
				break;
			}
		}
		if (hoverBox != prevHover) {
			for (BaseText txt : btList) {
				if (prevHover == txt.bounds()) {
					txt.mouseExit();
					break;
				}
			}
			for (BaseText txt : btList) {
				if (hoverBox == txt.bounds()) {
					txt.mouseEnter();
					break;
				}
			}			
			if (prevHover != null) repaint(prevHover);
			if (hoverBox != null)  repaint(hoverBox);
		}
	}
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {
		checkModifierKey(e);
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		mouseCommon(e, null);
		if (hoverBox == exitBox)
			doExitBoxAction();
		else if (hoverBox == defaultBox)
			doDefaultBoxAction();
		else if (hoverBox == userBox)
			doUserBoxAction();
		else if (hoverBox == lastBox)
			doLastBoxAction();
	}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {
		if (hoverBox != null) {
			hoverBox = null;
			repaint();
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		checkModifierKey(e);
		mouseCommon(null, e);
	}
}
