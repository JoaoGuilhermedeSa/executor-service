package org.acme.executor_service.task;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.acme.executor_service.model.Character;

public class CharacterTask implements Callable<String> {
	private final Character character;
	private final List<Character> enemies;
	private final Random random = new Random();

	public CharacterTask(Character character, List<Character> enemies) {
		this.character = character;
		this.enemies = enemies;
	}

	@Override
	public String call() throws Exception {
		if (!character.isAlive()) {
			return "";
		}
		if (character.isStunned()) {
			character.stun();
			return character.getName() + " está atordoado e não pode agir!";
		}

		if (!enemies.isEmpty()) {
			List<Character> aliveEnemies = enemies.stream().filter(enemy -> enemy.isAlive())
					.collect(Collectors.toList());
			if (aliveEnemies.size() == 0) {
				return doBuff();
			}
			Character enemy = aliveEnemies.get(random.nextInt(aliveEnemies.size()));
			if (!enemy.isAlive()) {
				return doBuff();
			}

			if (enemy.attemptBlock(random.nextDouble())) {
				return character.getName() + " tentou atacar " + enemy.getName() + ", mas o ataque foi bloqueado!";
			}

			int damage = character.getDamage() + random.nextInt(10);
			if (character.attemptCritical(random.nextDouble())) {
				damage *= 2;
				enemy.takeDamage(damage);
				enemy.stun();
				return character.getName() + " acertou um GOLPE CRÍTICO em " + enemy.getName() + " causando " + damage
						+ " de dano!";
			}
			enemy.takeDamage(damage);
		}

		return doBuff();
	}

	private String doBuff() {
		if (character.getRole().equals("Elder Druid")) {
			character.heal();
			return character.getName() + " se curou!";
		}

		if (character.getRole().equals("Master Sorcerer")) {
			character.applyDamageBuff();
			return character.getName() + " buffou seu dano!";
		}

		if (character.getRole().equals("Elite Knight")) {
			character.applyBlockBuff();
			return character.getName() + " buffou sua chance de bloqueio!";
		}

		if (character.getRole().equals("Royal Paladin")) {
			character.applyCriticalBuff();
			return character.getName() + " buffou sua chance de crítico!";
		}
		return "";
	}
}
