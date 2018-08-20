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
package games.stendhal.server.maps.tatry.mountain.house;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ZonaGoralaNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Gaździna Bożena") {

			/**
			 * Creates a path around the table with the beers and to the furnance.
			 */
			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(16, 15));
				nodes.add(new Node(16, 13));
				nodes.add(new Node(13, 13));
				nodes.add(new Node(13, 6));
				nodes.add(new Node(11, 6));
				nodes.add(new Node(11, 5));
				nodes.add(new Node(8, 5));
				nodes.add(new Node(8, 3));
				nodes.add(new Node(3, 3));
				nodes.add(new Node(8, 3));
				nodes.add(new Node(8, 5));
				nodes.add(new Node(11, 5));
				nodes.add(new Node(11, 6));
				nodes.add(new Node(13, 6));
				nodes.add(new Node(16, 13));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj. Co Ciebie sprowadza tutaj do nas?");
				addJob("Praca? Może znajdzie się coś. Pójdź do mego męża, pewnie on będzie miał jakieś zadanie dla Ciebie... jest na zewnątrz.");
				addHelp("Nie potrzebuję Twojej pomocy. Wyjdź na zewnątrz i spotaj się z moim mężem, on pewnie będzie miał jakieś ciekawe zadanie dla Ciebie.");
				addGoodbye("Jak będziesz czegoś potrzebował to wróć.");
			}
		};

		npc.setEntityClass("noimagenpc");
		npc.setPosition(16, 15);
		npc.initHP(100);
		zone.add(npc);
	}
}