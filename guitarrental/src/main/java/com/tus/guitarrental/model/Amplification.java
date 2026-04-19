package com.tus.guitarrental.model;

public sealed interface Amplification extends Instrument permits GuitarAmplifier, BassAmplifier, PublicAddressSystem {

}
