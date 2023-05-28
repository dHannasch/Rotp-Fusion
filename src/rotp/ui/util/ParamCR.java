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

import java.io.Serializable;

import rotp.model.empires.CustomRaceDefinitions;
import rotp.model.empires.Race;
import rotp.model.game.DynOptions;

public class ParamCR extends ParamObject {

	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 */
	public ParamCR(String gui, String name) {
		super(gui, name, null);
	}
	// ===== Overriders =====
	//
	@Override public Serializable defaultValue() {
		System.out.println("ParamCR defaultValue() {"); // TODO BR: REMOVE
		return CustomRaceDefinitions.getDefaultOptions();
	}
//	@Override public Serializable getOptionValue(IGameOptions options) {
//		if (super.get() == null)
//			return CustomRaceDefinitions.getDefaultOptions();
//		return super.get();
//	}
//	@Override public void updateOptionTool(DynamicOptions options) {
//		System.out.println("ParamCR.setOptionsTools");
//		if (options != null)
//			set((Serializable) options.getObject(getLangLabel(), creationValue()));
//	}
//	@Override public void updateOption(DynamicOptions options) {
//		System.out.println("ParamCR.setOptions");
//		if (options != null)
//			options.setObject(getLangLabel(), get());
//	}
	// ===== Other Methods =====
	//
	public Race getRace() {
		Race r = getCustomRace().getRace();
		System.out.println("ParamCR getRace(): " + r.name()); // TODO BR: REMOVE
		return r;
	}
	private CustomRaceDefinitions getCustomRace() {
		return new CustomRaceDefinitions((DynOptions) get());
	}
}
