hasValueName(vbc, "BenevolenceCaring").
hasValueName(vbd, "BenevolenceDependability").
hasValueName(vuc, "UniversalismConcern").
hasValueName(vun, "UniversalismNature").
hasValueName(vut, "UniversalismTolerance").
hasValueName(vst, "SelfDirectionThought").
hasValueName(vsa, "SelfDirectionAction").
hasValueName(vsi, "Stimulation").
hasValueName(vhe, "Hedonism").
hasValueName(vac, "Achievement").
hasValueName(vpd, "PowerDominance").
hasValueName(vpr, "PowerResources").
hasValueName(vfa, "Face").
hasValueName(vsp, "SecurityPersonal").
hasValueName(vss, "SecuritySocietal").
hasValueName(vtr, "Tradition").
hasValueName(vcr, "ConformityRules").
hasValueName(vci, "ConformityInterpersonal").
hasValueName(vhu, "Humility").

vl::defaultValueJustAbove(vbegin, vac).
vl::defaultValueJustAbove(vac, vbc).
vl::defaultValueJustAbove(vac, vun).
vl::defaultValueJustAbove(vbc, vhe).
vl::defaultValueJustAbove(vun, vhe).
vl::defaultValueJustAbove(vhe, vcr).
vl::defaultValueJustAbove(vcr, vbd).
vl::defaultValueJustAbove(vcr, vuc).
vl::defaultValueJustAbove(vcr, vut).
vl::defaultValueJustAbove(vcr, vst).
vl::defaultValueJustAbove(vcr, vsa).
vl::defaultValueJustAbove(vcr, vsi).
vl::defaultValueJustAbove(vcr, vpd).
vl::defaultValueJustAbove(vcr, vpr).
vl::defaultValueJustAbove(vcr, vfa).
vl::defaultValueJustAbove(vcr, vsp).
vl::defaultValueJustAbove(vcr, vss).
vl::defaultValueJustAbove(vcr, vtr).
vl::defaultValueJustAbove(vcr, vci).
vl::defaultValueJustAbove(vcr, vhu).
vl::defaultValueJustAbove(vbd, vend).
vl::defaultValueJustAbove(vuc, vend).
vl::defaultValueJustAbove(vut, vend).
vl::defaultValueJustAbove(vst, vend).
vl::defaultValueJustAbove(vsa, vend).
vl::defaultValueJustAbove(vsi, vend).
vl::defaultValueJustAbove(vpd, vend).
vl::defaultValueJustAbove(vpr, vend).
vl::defaultValueJustAbove(vfa, vend).
vl::defaultValueJustAbove(vsp, vend).
vl::defaultValueJustAbove(vss, vend).
vl::defaultValueJustAbove(vtr, vend).
vl::defaultValueJustAbove(vci, vend).
vl::defaultValueJustAbove(vhu, vend).

//defaultImportanceOfValues([vac, [vbc, vun], vhe, vcr, [vbc, vuc, vut, vst, vsa, vsi, vpd, vor, vfa, vsp, vss, vtr, vci, vhu]]).