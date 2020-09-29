decac -p ../deca/codegen/valid/sansObjet/*.deca
decac -p ../deca/codegen/valid/objet/*.deca
decac -p ../deca/context/valid/sansObjet/*.deca
decac -p ../deca/context/valid/objet/*.deca
decac -p ../deca/context/valid/auto/valid/*.deca

decac -v ../deca/codegen/valid/sansObjet/*.deca
decac -n ../deca/codegen/valid/sansObjet/Factoriel.deca
decac -d ../deca/codegen/valid/sansObjet/Factoriel.deca
decac -d -d ../deca/codegen/valid/sansObjet/Factoriel.deca
decac -d -d -d ../deca/codegen/valid/sansObjet/Factoriel.deca
decac -r 12 ../deca/codegen/valid/sansObjet/Factoriel.deca
decac -P ../deca/codegen/valid/sansObjet/bigDeca.deca ../deca/codegen/valid/sansObjet/bigDeca.deca
decac -b
decac
