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

package rotp.ui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;

public class ParamFloat extends AbstractParam<Float> {
	
	private String guiFormat = "%";
	private String cfgFormat = "0.0##";
	
	// ========== Constructors ==========
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamFloat(String gui, String name, Float defaultValue) {
		super(gui, name, defaultValue, null, null, true, 1.0f, 1.0f, 1.0f);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param loop     what to do when reaching the limits
	 */
	public ParamFloat(String gui, String name, Float defaultValue
			, Float minValue, Float maxValue, boolean loop) {
		super(gui, name, defaultValue, minValue, maxValue, loop, 1.0f, 1.0f, 1.0f);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param loop     what to do when reaching the limits
	 */
	public ParamFloat(String gui, String name, Float defaultValue
			, Float minValue, Float maxValue, boolean loop
			, Float baseInc, Float shiftInc, Float ctrlInc) {
		super(gui, name, defaultValue, minValue, maxValue, loop, baseInc, shiftInc, ctrlInc);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param loop     What to do when reaching the limits
	 * @param cfgFormat String decimal formating for Remnant.cfg: default value = "%"
	 * @param guiFormat String decimal formating for GUI display: default value = "0.0##"
	 */
	public ParamFloat(String gui, String name, Float defaultValue
			, Float minValue, Float maxValue, boolean loop
			, Float baseInc, Float shiftInc, Float ctrlInc
			, String cfgFormat, String guiFormat) {
		super(gui, name, defaultValue, minValue, maxValue, loop, baseInc, shiftInc, ctrlInc);
		this.cfgFormat = cfgFormat;
		this.guiFormat = guiFormat;
	}
	// ========== Overriders ==========
	//
	@Override public String getCfgValue() {
		if (isCfgPercent()) {
			return String.format("%d", (int) (value * 100f));
		}
		return new DecimalFormat(cfgFormat).format(value);
	}
	@Override public String getGuiValue() {
		if (isGuiPercent()) {
			return String.format("%d", (int) (value * 100f));
		}
		return new DecimalFormat(guiFormat).format(value);
	}
	@Override public Float setFromCfg(String newValue) {
		if (isCfgPercent()) {
			value = stringToInteger(newValue.replace("%", "")) / 100f;
		} else {
			value = stringToFloat(newValue);
		}
		return value;
	}	
	@Override public Float next() {
		return next(baseInc);
	}
	@Override public Float prev() {
		return next(-baseInc); 
	}
	@Override public Float toggle(MouseEvent e)	{
		return next(getInc(e) * getDir(e));
	}
	@Override public Float toggle(MouseWheelEvent e) {
		return next(getInc(e) * getDir(e));
	}
	// ========== Other Methods ==========
	//
	public Float next(float i) {
		if (i == 0) return setToDefault(true);
		value+=i;
		if (maxValue != null && value > maxValue) {
			if (minValue != null)
				return setAndSave(minValue);
			else
				return setAndSave(maxValue);
		}
		else if (minValue != null && value < minValue) {
			if (maxValue != null)
				return setAndSave(maxValue);
			else
				return setAndSave(minValue);
		}
		return setAndSave(value);
	}
	private boolean isGuiPercent() {
		return guiFormat.equals("%");
	}
	private boolean isCfgPercent() {
		return cfgFormat.equals("%");
	}
}
