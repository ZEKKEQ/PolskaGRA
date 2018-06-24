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
package games.stendhal.server.maps.pol.wieliczka.mines;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Skille250NPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param zone
	 *            The zone to be configured.
	 * @param attributes
	 *            Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildMineArea(zone);
	}

	private void buildMineArea(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Arisa") {

			
			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(118, 24));
				nodes.add(new Node(118, 20));
				nodes.add(new Node(117, 20));
				nodes.add(new Node(117, 19));
				setPath(new FixedPath(nodes, true));
			}
			
			@Override
			protected void createDialog() {
				addGreeting(null, new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						String reply = "Witaj! Jestem tutaj aby #nauczyć Cię czegoś o walce z potworami!";

						if (player.getLevel() < 250) {
							reply += " Jeszcze nie jesteś godzien! Osiągnij 250 poziom!";
						} else {
							reply += " Jesteś godzień przyjąć moje nauki.";
						}
						raiser.say(reply);
					}
				});

				addReply("nauczyć",
						"Gdy osiągniesz 250 poziom nauczę Cię lepiej walczyć z potworami.");
				addGoodbye();
			}
		};


		npc.addInitChatMessage(null, new ChatAction() {
			@Override
			public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
				if (!player.hasQuest("ArisaReward")
						&& (player.getLevel() >= 250)) {
					player.setQuest("ArisaReward", "done");

					player.setAtkXP(700000 + player.getAtkXP());
					player.setDefXP(2080000 + player.getDefXP());
					player.addXP(20000);

					player.incAtkXP();
					player.incDefXP();
				}

				if (!player.hasQuest("ArisaFirstChat")) {
					player.setQuest("ArisaFirstChat", "done");
					((SpeakerNPC) raiser.getEntity()).listenTo(player, "hi");
				}
				
			}
			
		});

		npc.setEntityClass("blackwizardpriestnpc");
		npc.setPosition(118, 24);
		npc.initHP(85);
		zone.add(npc);
	}
}