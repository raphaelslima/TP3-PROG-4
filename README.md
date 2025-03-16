## Relatório TP 3 - Prog IV

Integrantes: João Pedro Silva, Nathan Siqueira, Raphael Lima

O trabalho consistiu na implementação de uma árvore B para organizar e pesquisar números em arquivos grandes, onde não é possível carregar o arquivo inteiro em memória. O primeiro passo foi criar uma árvore B convencional. Para testar as operações de busca, inserção e remoção, utilizamos um arquivo pequeno, conseguindo realizar as operações sem problemas. Essa foi a primeira parte da atividade.

Depois seguimos para a parte do desafio, onde tivemos uma dificuldade maior no processo de visualizar como funcionaria a lógica para as folhas serem pontos de referência para um arquivo em memória secundária. Percebemos uma similaridade do funcionamento do sistema com a árvore B+, onde os valores dos nós superiores são passados para nós inferiores de forma a criar uma cópia do arquivo principal ordenado nas folhas, podendo assim ligá-las com ponteiros e formar uma estrutura sequencial.

Já que não foi especificado o uso de uma árvore B+ no enunciado, tentamos pensar em soluções usando a árvore B convencional. Pensamos na possibilidade de usar arquivos individuais para cada um de nós da árvore, dividindo o arquivo completo por todos os nós e guardando uma referência desse arquivo em uma árvore de referências. Essa árvore de referências conteria o vínculo com o arquivo principal e a janela de valores que cada parte continha. No entanto, essa ideia foi descartada por dificuldades em criar uma estrutura funcional para gerenciar essas referências.
Depois de algumas tentativas, decidimos fazer com que todos os nós da árvore B armazenassem referências diretas dos filhos. Assim, ao buscar um número, a árvore indicaria qual página carregar, sem precisar carregar o arquivo inteiro em memória. Isso permitiu que a busca, inserção e remoção funcionassem corretamente para arquivos grandes, carregando apenas as partes necessárias.

A maior dúvida foi entender como uma árvore B poderia ter apenas nós folhas que armazenassem referências em vez de números diretamente. 