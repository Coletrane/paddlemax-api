#!/bin/sh

cd sql;
psql -w -U coletrane -f 001.sql;
