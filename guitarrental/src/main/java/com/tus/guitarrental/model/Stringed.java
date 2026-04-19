package com.tus.guitarrental.model;

public sealed interface Stringed extends Instrument permits Guitar, Bass {
	int numberOfStrings();

}
