# CPLSI [![AUR](https://img.shields.io/aur/license/yaourt.svg?style=flat-square)](LICENSE.md)

CPLSI  Copyright (C) 2016  Vladyslav Vasyliev

This is an implementation of the Clustering with Partition Level Side
Information algorithm. Based on method presented in paper: "Hongfu Liu
and Yun Fu, _Clustering with Partition Level Side Information_. IEEE 
International Conference on Data Mining, 877-882, 2015."

Provided algorithm solving a K-means-like optimization problem. It has
small modifications on a distance function and update rule for the 
centroids.

The Normalized Mutual Information (NMI) is used to measure the 
clustering performance.

**Important!**
Content of the _'data_sets'_ folder is not the part of this project.
License agreement of the project does not apply to it.

Latest build provided in _target_ folder. It automatically performs 
calculations on the hardcoded files.
