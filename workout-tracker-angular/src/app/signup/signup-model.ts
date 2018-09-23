export class SignupModel {

  constructor(
    public firstname?: string,
    public lastname?: string,
    public email?: string,
    public password?: string) { }

  toString() {return JSON.stringify(this); }

}
