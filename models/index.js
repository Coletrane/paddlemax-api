const fs = require('fs')
const path = require('path')
const Sequelize = require('sequelize')
const basename = path.basename(__filename)

const env = process.env.NODE_ENV || 'development'
const config = require(path.join(__dirname, '/../config/db.js'))[env]

let db = {}

const sequelize = new Sequelize(
  config.database,
  config.username,
  config.password,
  config
)

fs
  .readdirSync(__dirname)
  .filter(file => {
    return (file.indexOf('.') !== 0) && (file !== basename) && (file.slice(-3) === '.js')
  })
  .forEach(file => {
    const model = sequelize['import'](path.join(__dirname, file))
    db[model.name] = model
  })

Object.keys(db).forEach(model => {
  if (db[model].associate) {
    db[model].associate(db)
  }
})

db.sequelize = sequelize
db.Sequelize = sequelize

module.exports = db
