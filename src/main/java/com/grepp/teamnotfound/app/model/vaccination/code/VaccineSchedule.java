package com.grepp.teamnotfound.app.model.vaccination.code;

public enum VaccineSchedule {
    BOOSTER,
    ADDITIONAL;

    public String getWords(){
        return switch (this){
            case BOOSTER -> " 추가_접종일";
            case ADDITIONAL -> " 보강_접종일";
        };
    }
}
