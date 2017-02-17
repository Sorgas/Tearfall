package com.model.objects;

import com.model.localmap.Position;

public abstract class GameObject {
	private Position position;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
