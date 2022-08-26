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

import static rotp.ui.util.SettingBase.CostFormula.RELATIVE;
import static rotp.util.Base.random;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class SettingFloat extends SettingBase<Float> {

	private static final boolean defaultIsList			= false;
	private static final boolean defaultIsBullet		= false;
	private static final boolean defaultLabelsAreFinals	= false;
	private static final boolean defaultSaveAllowed		= true;
	private static final Float defaultBaseInc	= 1f;
	private static final Float defaultShiftInc	= 1f;
	private static final Float defaultCtrlInc	= 1f;
	private static final int   randCount		= 50;
	
	private boolean loop	= false;
	private Float minValue	= null;
	private Float maxValue	= null;
	private Float baseInc	= defaultBaseInc;
	private Float shiftInc	= defaultShiftInc;
	private Float ctrlInc	= defaultCtrlInc;
	private float[] posCostFactor;
	private float[] negCostFactor;
	private CostFormula costFormula = RELATIVE;
	private boolean useNegFormula	= false;
	private float	rawBaseCost		= 0f;

	// ========== constructors ==========
	//
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 */
	public SettingFloat(String guiLangLabel, String nameLangLabel, Float defaultValue
			, Float minValue, Float maxValue) {
		super(guiLangLabel, nameLangLabel, defaultValue,
				defaultIsList, defaultIsBullet, defaultLabelsAreFinals, defaultSaveAllowed);
		put("-", "-", 0f, defaultValue);
		this.minValue	= minValue;
		this.maxValue	= maxValue;
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 */
	public SettingFloat(String guiLangLabel, String nameLangLabel, Float defaultValue) {
		this(guiLangLabel, nameLangLabel, defaultValue, null, null);
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param loop Once an end is reached, go to the other end
	 */
	public SettingFloat(String guiLangLabel, String nameLangLabel, Float defaultValue
			, Float minValue, Float maxValue, boolean loop) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue);
		this.loop = loop;
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 */
	public SettingFloat(String guiLangLabel, String nameLangLabel, Float defaultValue
			, Float minValue, Float maxValue
			, Float baseInc, Float shiftInc, Float ctrlInc) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue);
		this.baseInc	= baseInc;
		this.shiftInc	= shiftInc;
		this.ctrlInc	= ctrlInc;
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param saveAllowed  To allow the parameter to be saved in Remnants.cfg
	 * @param costFormula Formula type to establish a cost
	 * @param costFactor To establish a cost
	 */
	public SettingFloat(String guiLangLabel, String nameLangLabel, Float defaultValue
			, Float minValue, Float maxValue
			, Float baseInc, Float shiftInc, Float ctrlInc
			, boolean saveAllowed, CostFormula costFormula, float... costFactor) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue, baseInc, shiftInc, ctrlInc,
				saveAllowed, costFormula, costFactor, costFactor);
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param saveAllowed  To allow the parameter to be saved in Remnants.cfg
	 * @param costFormula Formula type to establish a cost
	 * @param costFactor To establish a cost
	 */
	public SettingFloat(String guiLangLabel, String nameLangLabel, Float defaultValue
			, Float minValue, Float maxValue
			, Float baseInc, Float shiftInc, Float ctrlInc
			, boolean saveAllowed, CostFormula costFormula, float[] posCostFactor, float[] negCostFactor) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue, baseInc, shiftInc, ctrlInc);
		this.costFormula = costFormula;
		this.posCostFactor	= posCostFactor;
		this.negCostFactor	= negCostFactor;
		saveAllowed(saveAllowed);
	}

	// ===== Overriders =====
	//
	@Override public Float randomize(float avg, float stDev) {
		float rand = avg + (stDev * (float) random.nextGaussian());
		float step = Math.abs(maxValue - minValue) / (randCount - 1);
		float bestVal = defaultValue();
		float bestDev = Math.abs(rand - settingCost(bestVal));
		float limVal  = maxValue + step/2;
		for (float testVal=minValue; testVal<limVal; testVal+=step) {
			float dev = Math.abs(rand - settingCost(testVal));
			if (dev < bestDev) {
				bestVal = testVal;
				bestDev = dev;
			}
		}
		return bestVal;
	}
	@Override public void setFromCfgValue(String newValue) {
		set(stringToFloat(newValue));
	}	
	@Override public void next() {
		next(baseInc);
	}
	@Override public void prev() {
		 next(-baseInc);
	}
	@Override public void toggle(MouseEvent e) {
		next(getInc(e) * getDir(e));
	}
	@Override public void toggle(MouseWheelEvent e) {
		next(getInc(e) * getDir(e));
	}
	@Override public float settingCost() {
		return settingCost(settingValue());
	}
	@Override public SettingFloat saveAllowed(boolean allowed){
		super.saveAllowed(allowed);
		return this;
	}
	// ===== Other Methods =====
	//
	private void next(Float i) {
		if (i == 0) {
			setToDefault(true);
			return;
		}
		Float value = settingValue() + i;
		if (maxValue != null && value > maxValue) {
			if (loop && minValue != null)
				setAndSave(minValue);
			else
				setAndSave(maxValue);
			return;
		}
		else if (minValue != null && value < minValue) {
			if (loop && maxValue != null)
				setAndSave(maxValue);
			else
				setAndSave(minValue);
			return;
		}
		setAndSave(value);
	}
	private float settingCost(Float value) {
		float baseCost = getBaseCost(value);
		if (posCostFactor == null)
			return baseCost;
		float cost = 0;
		if (useNegFormula)
			for (int i=0; i<negCostFactor.length; i++)
				cost -= negCostFactor[i] * Math.pow(baseCost, i);
		else
			for (int i=0; i<posCostFactor.length; i++)
				cost += posCostFactor[i] * Math.pow(baseCost, i);			
		return cost;
	}
	private float getBaseCost() {
		return getBaseCost(settingValue());
	}
	private float getBaseCost(Float value) {
		switch (costFormula) {
		case DIFFERENCE:
			rawBaseCost   = value - defaultValue();
			useNegFormula = rawBaseCost < 0f;
			return Math.abs(rawBaseCost);
		case RELATIVE:
			rawBaseCost   = Math.abs((float)value / defaultValue());
			useNegFormula = rawBaseCost < 1f;
			if (rawBaseCost > 1f)
				return rawBaseCost-1;
			else
				return (1/rawBaseCost)-1;
		}
		useNegFormula = false;
		return 0f;
	}
	private Float getInc(MouseEvent e) {
		if (e.isShiftDown()) 
			return shiftInc;
		else if (e.isControlDown())
			return ctrlInc;
		else
			return baseInc;
	}
	private Float getInc(MouseWheelEvent e) {
		if (e.isShiftDown()) 
			return shiftInc;
		else if (e.isControlDown())
			return ctrlInc;
		else
			return baseInc;
	}
	/**
	 * Convert String to Float and manage errors
	 * @param string Source of Float
	 * @return Float value, or <b>null</b> on error
	 */
	static Float stringToFloat(String string) {
		try {
			return Float.valueOf(string.trim());
		}
		catch (NumberFormatException nfe) {
			return null; // silent error!
		}
	}
}
