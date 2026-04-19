package com.tus.guitarrental.entities;

public sealed interface Amplification extends Instrument permits GuitarAmplifier, BassAmplifier, PublicAddressSystem {

}
