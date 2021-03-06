/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.script;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kymara
 *
 * Not safe for players below level 10
 */
public class BeholderRaid extends CreateRaid {

	@Override
	protected Map<String, Integer> createArmy() {
		final Map<String, Integer> attackArmy = new HashMap<String, Integer>();
		attackArmy.put("oczko", 7);
		attackArmy.put("zielony glut", 4);
		attackArmy.put("oko", 5);
		attackArmy.put("oko starsze", 1);
		attackArmy.put("wąż", 3);
		attackArmy.put("żmija", 4);
		return attackArmy;
	}

	@Override
	protected String getInfo() {
		return "Nie jest bezpieczna dla wojowników poniżej 12 poziomu";
	}
}
