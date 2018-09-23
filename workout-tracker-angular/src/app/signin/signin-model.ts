export class SigninModel {

  constructor(
    public username?: string,
    public password?: string,
    public authToken?: string
  ) { }

  toString() {return JSON.stringify(this); }

}
