module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('User', {
    firstName: {
      type: DataTypes.STRING,
      allowNull: false
    },
    lastName: {
      type: DataTypes.STRING,
      allowNull: false
    },
    email: {
      type: DataTypes.STRING,
      allowNull: false,
      unique: true
    },
    location: DataTypes.STRING,
    weightLbs: DataTypes.INTEGER,
    birthday: DataTypes.DATE,
    facebookId: DataTypes.STRING,
    googleId: DataTypes.STRING
  })

  return User
}
