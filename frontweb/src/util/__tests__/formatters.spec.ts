import { formatPrice } from "util/formatters";

describe('forrmatPrice for positive numbers', () =>{

  test('formatPrice should format number pt-Br when give 10.1', () =>{
    const value = 10.1;
    const result = formatPrice(value);
    expect(result).toEqual("10,10");
   });

   test('formatPrice should format number pt-Br when give 0.1', () =>{
    const result = formatPrice(0.1);
    expect(result).toEqual("0,10");
   });

})

describe('forrmatPrice for non-positive numbers', () =>{

  test('formatPrice should format number pt-Br when give 0', () =>{
    const value = 0;
    const result = formatPrice(value);
    expect(result).toEqual("0,00");
   });

   test('formatPrice should format number pt-Br when give -5,1', () =>{
    const result = formatPrice(-5.1);
    expect(result).toEqual("-5,10");
   });

})

