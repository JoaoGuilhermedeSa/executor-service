package org.acme.executor_service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.acme.executor_service.model.Character;
import org.acme.executor_service.task.CharacterTask;

public class App {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(6);
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		Runnable battleRound = new Runnable() {

			@Override
			public void run() {
				List<Character> teamA = List.of(new Character("Eternal Oblivion", "Elite Knight"),
						new Character("Cachero", "Master Sorcerer"), new Character("Lord Quiz", "Royal Paladin"),
						new Character("Mateusz Dragon Wielki", "Elder Druid"));

				List<Character> teamB = List.of(new Character("Bubble", "Elite Knight"),
						new Character("Taifun Devilry", "Master Sorcerer"), new Character("Lord Stax", "Royal Paladin"),
						new Character("Dejairzin", "Elder Druid"));
				
				System.out.println("Iniciando combate.");

				while (teamA.stream().anyMatch(Character::isAlive) && teamB.stream().anyMatch(Character::isAlive)) {
					List<Future<String>> results = new ArrayList<>();

					results.addAll(teamA.parallelStream().map(character -> executor.submit(new CharacterTask(character, teamB))).toList());

					results.addAll(teamB.parallelStream().map(character -> executor.submit(new CharacterTask(character, teamA))).toList());

					results.forEach(result -> {
						try {
							String text = result.get();
							if (!text.equals("")) {
								System.out.println(text);
							}
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					});
				}

				System.out.println(teamA.stream().anyMatch(Character::isAlive) ? "Time A venceu!" : "Time B venceu!");

//				executor.shutdown();
//				scheduler.shutdown();
			}
		};
		scheduler.scheduleAtFixedRate(battleRound, 0, 20, TimeUnit.SECONDS);

	}
}
