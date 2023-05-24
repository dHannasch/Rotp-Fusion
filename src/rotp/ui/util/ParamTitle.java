/*
 * Copyright 2015-2020 Ray Fowler
 *
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rotp.ui.util;

import static rotp.model.game.BaseOptionsTools.HEADERS;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.model.game.IGameOptions;
import rotp.ui.game.BaseModPanel;

public class ParamTitle extends AbstractParam<String> {
	
	// ===== Constructors =====
	//
	/**
	 * @param title The title
	 */
	public ParamTitle(String title) {
		super(HEADERS, title, "");
	}

	// ===== Overriders =====
	//
	@Override public void setFromCfgValue(String newValue) { value(newValue); }	
	@Override public void prev() {}
	@Override public void next() {}
	@Override public void toggle(MouseWheelEvent e)	{}
	@Override public void toggle(MouseEvent e, BaseModPanel frame)	 {}
	@Override public void setOptionTools() {}
	@Override public void setOptions()	 {}
	@Override protected String getOptionValue(IGameOptions options) { return ""; }
	@Override protected void setOptionValue(IGameOptions options, String value) {}
	@Override public boolean isTitle()	{ return true; }
	@Override public String getGuide()	{ return headerHelp(false); }

}
