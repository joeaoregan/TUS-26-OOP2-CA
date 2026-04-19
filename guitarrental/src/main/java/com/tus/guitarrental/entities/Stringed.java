package com.tus.guitarrental.entities;

public sealed interface Stringed extends Instrument permits Guitar, Bass {
	int numberOfStrings();

}
