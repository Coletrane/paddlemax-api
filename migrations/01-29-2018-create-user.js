const User = require('../models').User

module.exports = {
  up: (queryInterface, Sequelize) => {
    return queryInterface.createTable('PaddleMaxUsers', User)
  }
}
