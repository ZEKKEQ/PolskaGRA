package games.stendhal.server.maps.quests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;

public class KrolewskiPlaszcz extends AbstractQuest {
	public static final String QUEST_SLOT = "krolewski_plaszcz";

	private void start() {
		final SpeakerNPC npc = npcs.get("Król Krak");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES,
			new QuestNotCompletedCondition(QUEST_SLOT),
			ConversationStates.QUEST_OFFERED,
			"Potrzebuję nowego płaszczu królewskiego. Aktualny mój płaszcz się powoli niszczy. Potrzebuję od Ciebie 10 sztuk #'płaszcz z czarnego smoka'. Przyniósłbyś byś mi to?",
			null);

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES,
			new QuestCompletedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"Jako król państwa Polan, dziękuję Ci za pomoc!",
			null);

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Świetnie... Będę za tobą czekał mieszczaninie.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 5.0));

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Może nie zasługujesz na miano rycerza... Jeszcze...",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -15.0));
	}

	private void done() {
		final SpeakerNPC npc = npcs.get("Król Krak");

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestStateStartsWithCondition(QUEST_SLOT, "start"),
					new PlayerHasItemWithHimCondition("czarny płaszcz smoczy",10)),
			ConversationStates.ATTENDING,
			"Widzę, że masz przy sobie płaszcze z czarnego smoka. Czy są one dla mnie?", null);

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestStateStartsWithCondition(QUEST_SLOT, "start"),
					new NotCondition(new PlayerHasItemWithHimCondition("czarny płaszcz smoczy",10))),
			ConversationStates.ATTENDING,
			"Nie będę się powtarzał...",
			null);

		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new DropItemAction("czarny płaszcz smoczy",10));
		reward.add(new IncreaseXPAction(10000));
		reward.add(new EquipItemAction("tarcza cieni", 1, true));
		reward.add(new SetQuestAction(QUEST_SLOT, "done"));
		reward.add(new IncreaseKarmaAction(15));
		npc.add(
			ConversationStates.ATTENDING,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Dziękuję Ci za pomoc! Teraz mój królewski krawiec uszyje dla mnie nowy królewski płaszcz.",
			new MultipleActions(reward));

		npc.add(
			ConversationStates.ATTENDING,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Chyba o coś Ciebie prosiłem, prawda?",
			null);
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("Rozmawiałem z królem Krakiem.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("Nie zamierzam pomóc królowi.");
		}
		if (player.isQuestInState(QUEST_SLOT, "start", "done")) {
			res.add("Pomogę królowi.");
		}
		if ("start".equals(questState) && player.isEquipped("płaszcz czarnego smoka", 10) || "done".equals(questState)) {
			res.add("Mam 10 płaszczy dla Króla Kraka.");
		}
		if ("done".equals(questState)) {
			res.add("Oddałem płaszcze królowi.");
		}
		return res;
	}
	
	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Królewski Płaszcz",
				"Król Krak - władca królewskiego państwa Polan potrzebuje nowego królewskiego płaszcza.",
				false);
		start();
		done();
	}

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public String getName() {
		return "KrolewskiPlaszcz";
	}

	public String getTitle() {
		return "Królewski płaszcz";
	}

	@Override
	public String getRegion() {
		return Region.KRAKOW_CITY;
	}

	@Override
	public String getNPCName() {
		return "Król Krak";
	}
}
