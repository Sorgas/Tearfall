package com.utils;

/**
 * Created by Alexander on 03.03.2017.
 */
public class Plane {
	private float xMod;
	private float yMod;
	private float zMod;
	private float d;

	public Plane(Position pos1, Position pos2, Position pos3) {
		xMod = (pos2.getY() - pos1.getY()) * (pos3.getZ() - pos1.getZ()) - (pos2.getZ() - pos1.getZ()) * (pos3.getY() - pos1.getY());
		yMod = -(pos2.getX() - pos1.getX()) * (pos3.getZ() - pos1.getZ()) + (pos2.getZ() - pos1.getZ()) * (pos3.getX() - pos1.getX());
		zMod = (pos2.getX() - pos1.getX()) * (pos3.getY() - pos1.getY()) - (pos2.getY() - pos1.getY()) * (pos3.getX() - pos1.getX());
		d = -(yMod * pos1.getY() + xMod * pos1.getX() + zMod * pos1.getZ());
	}

	public float getZ(float x, float y) {
		return -(d + (xMod * x) + (yMod * y)) / zMod;
	}

	public float getX(float y, float z) {
		return -(d + (zMod * z) + (yMod * y)) / xMod;
	}

	public float getY(float x, float z) {
		return -(d + (xMod * x) + (zMod * z)) / yMod;
	}
}
