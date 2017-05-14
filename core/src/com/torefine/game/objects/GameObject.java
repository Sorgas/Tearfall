package com.torefine.game.objects;

import com.utils.Position;

public abstract class GameObject {
	private Position position;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
