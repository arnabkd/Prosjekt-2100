        .data                           
        .globl  LF                      
LF:     .fill   4                       
        .text                           
        .globl  gcd                     
gcd:    pushl   %ebp                    # int gcd;
        movl    %esp,%ebp               
.L0001:                                 # Start while-statement
        movl    8(%ebp),%eax            # Putting a in %eax
        pushl   %eax                    
        movl    12(%ebp),%eax           # Putting b in %eax
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setne   %al                     # Compare operator : !=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0002                  
                                        # Start if-statement
        movl    8(%ebp),%eax            # Putting a in %eax
        pushl   %eax                    
        movl    12(%ebp),%eax           # Putting b in %eax
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setl    %al                     # Compare operator : <
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0004                  
        movl    12(%ebp),%eax           # Putting b in %eax
        pushl   %eax                    
        movl    8(%ebp),%eax            # Putting a in %eax
        movl    %eax,%ecx               
        popl    %eax                    
        subl    %ecx,%eax               # Subtracting
        movl    %eax,12(%ebp)           # b being assigned
        jmp     .L0003                  # End if-statement
.L0004:                                 # Start else-statement
        movl    8(%ebp),%eax            # Putting a in %eax
        pushl   %eax                    
        movl    12(%ebp),%eax           # Putting b in %eax
        movl    %eax,%ecx               
        popl    %eax                    
        subl    %ecx,%eax               # Subtracting
        movl    %eax,8(%ebp)            # a being assigned
.L0003:                                 # End else-statement
        jmp     .L0001                  
.L0002:                                 # End while-statement
        movl    8(%ebp),%eax            # Putting a in %eax
        jmp     .exit$gcd               # return-statement for gcd
.exit$gcd:                                
        popl    %ebp                    
        ret                             # end gcd
        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $8,%esp                 # Allocate 8 bytes
        movl    $10,%eax                # 10
        movl    %eax,LF                 # LF being assigned
        call    getint                  # call getint
        movl    %eax,-4(%ebp)           # v1 being assigned
        call    getint                  # call getint
        movl    %eax,-8(%ebp)           # v2 being assigned
.exit$main:                                
        addl    $8,%esp                 # Free 8 bytes
        popl    %ebp                    
        ret                             # end main
