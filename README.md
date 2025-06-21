# PROP-FIB

This repository contains the project for the Programming-Oriented Project ([PROP](https://www.fib.upc.edu/ca/estudis/graus/grau-en-enginyeria-informatica/pla-destudis/assignatures/PROP)) course at FIB-UPC.

## Project Overview

The goal of this project is to optimize the placement of products on a single circular shelf in a supermarket to maximize customer purchases. Customers are more likely to buy a product if it is placed near a related product (e.g., beer placed next to chips). Each pair of products has a known similarity score in the range [0,1] that represents how related they are. The system manages products and their similarity scores and computes an optimal product arrangement using at least two algorithms: a basic algorithm (e.g., brute force or greedy) and an approximation algorithm.

## Implementation 

Our implementation is written in Java, following a three-layer architecture and providing a user-friendly interface. We have implemented the following algorithms:

- Kruskal's Algorithm
- Hill Climbing
- Backtracking enhanced with a cutoff mechanism

## Code

The project is organized into different directories, each corresponding to a stage in the project development. Each directory contains its own documentation.

## Authors
- [Nil Casas Duatis](mailto:nil.casas.duatis@estudiantat.upc.edu)
- [Andreu Corden Moragrega](mailto:andreu.corden@estudiantat.upc.edu)
- [David Vergel Payán](mailto:david.vergel@estudiantat.upc.edu)

## License
### GNU GENERAL PUBLIC LICENSE  
Version 3, June 29, 2007

© 2024 Nil Casas Duatis, Andreu Corden Moragrega, David Vergel Payán

This software is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You can obtain a copy of the GNU General Public License at: https://www.gnu.org/licenses/gpl-3.0.html

If you modify this software and distribute it, you must include a statement indicating that you have made changes, and you must not conceal that the software has been modified.
