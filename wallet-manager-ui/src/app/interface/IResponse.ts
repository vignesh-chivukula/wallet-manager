export interface IResponse{
    responseMessage:String;
    httpStatus: any;
    responseStatus: String;
    responseBody: any;
}

export interface ITransactionDetail{
    transactionId: number;
    transactionDate: Date;
    transactionType: String;
    transactionDetail: String;
    transactionCategory: String;
    trasactionCost: number;
    transactionSource: String;
    userId: number;

}

export interface ITransaction{
    transactionDate: Date;
    transactionType: String;
    transactionDetail: String;
    transactionCategory: String;
    trasactionCost: number;
    userId: number;
}

export interface IChartDisplayData{
    y: number;
    name: String;
}

export interface IUser{
    name: String;
    email:String;
    password:String;
    splitwise_key:String;
}

export interface Food {
    value: string;
    viewValue: string;
}

export interface LinkToken {
    link_token:string
}

export interface ILinkedAccounts {
    accountName: string;
    accountType: string;
    institutionName: string;
    isActive: Boolean;
}

  