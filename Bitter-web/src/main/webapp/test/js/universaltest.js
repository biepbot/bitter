/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

QUnit.test( "replaceAll test", function( assert ) {
    assert.equal(replaceAll("aaabcaappAAa", "a", ""), "bcppAA");
    assert.equal(replaceAll("aaabcaappAAa", "b", ""), "aaacaappAAa");
    assert.equal(replaceAll("aaabcaappAAa", "A", "a"), "aaabcaappaaa");
});