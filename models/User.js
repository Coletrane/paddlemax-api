module.exports = (sequelize, DataTypes) => {
  let User = sequelize.define('User', {
    firstName: DataTypes.STRING,
    lastName: DataTypes.STRING,
    email: DataTypes.STRING,
    location: DataTypes.STRING,
    weightLbs: DataTypes.INTEGER,
    birthday: DataTypes.DATE,
    facebookId: DataTypes.STRING,
    facebookAuthToken: DataTypes.STRING,
    googleId: DataTypes.STRING
  })
}
